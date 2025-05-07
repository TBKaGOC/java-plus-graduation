package ru.practicum.stats.aggregator.handler;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.stats.avro.ActionTypeAvro;
import ru.practicum.ewm.stats.avro.EventSimilarityAvro;
import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.practicum.stats.aggregator.exception.IncorrectActionTypeException;
import ru.practicum.stats.aggregator.kafka.SimilarityProducer;

import javax.swing.event.ListDataEvent;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserActionHandler {
    final SimilarityProducer producer;
    // Map<Event, Map<User, Weight>>
    final Map<Long, Map<Long, Float>>  usersFeedback;
    // Map<Event, Map<Event, MinSum>>
    final Map<Long, Map<Long, Float>> eventsMinWeightSum;


    @Autowired
    public UserActionHandler(SimilarityProducer producer) {
        this.producer = producer;
        usersFeedback = new HashMap<>();
        eventsMinWeightSum = new HashMap<>();
    }

    public void handle(UserActionAvro avro) throws IncorrectActionTypeException {
        Long userId = avro.getUserId();
        Long eventId = avro.getEventId();
        Float weight = convertActionToWeight(avro.getActionType());
        Map<Long, Float> userRatings = new HashMap<>(usersFeedback.computeIfAbsent(eventId, key -> Map.of(userId, weight)));

        if (!userRatings.containsKey(userId) || userRatings.get(userId) < weight) {
            Float oldWeight = userRatings.getOrDefault(userId, 0.0F);
            userRatings.put(userId, weight);
            usersFeedback.put(eventId, userRatings);
            determineSimilarity(eventId, userId, oldWeight, weight, avro.getTimestamp());
        } else if (userRatings.size() == 1) {
            determineSimilarity(eventId, userId, 0.0F, weight, avro.getTimestamp());
        }
    }

    public void flush() {
        producer.flush();
    }

    public void close() {
        producer.close();
    }

    private void determineSimilarity(Long eventId, Long userId, Float oldWeight, Float newWeight, Instant timestamp) {
        List<Long> eventsWithSameUsers = usersFeedback.keySet().stream()
                .filter(e -> usersFeedback.get(e).containsKey(userId) && !Objects.equals(e, eventId))
                .toList();

        for (Long convergenceEvent: eventsWithSameUsers) {
            Float convergenceWeight = usersFeedback.getOrDefault(convergenceEvent, new HashMap<>())
                    .getOrDefault(userId, 0.0F);
            Long first = Math.min(eventId, convergenceEvent);
            Long second = Math.max(eventId, convergenceEvent);
            Float oldSum = eventsMinWeightSum.getOrDefault(first, new HashMap<>()).getOrDefault(second, 0.0F);
            Float newSum = oldSum - Math.min(oldWeight, convergenceWeight) + Math.min(newWeight, convergenceWeight);
            Map<Long, Float> userRating = new HashMap<>(eventsMinWeightSum.getOrDefault(first, new HashMap<>()));
            userRating.put(second, newSum);
            eventsMinWeightSum.put(first, userRating);

            producer.sendMessage(
                    EventSimilarityAvro.newBuilder()
                            .setEventA(first)
                            .setEventB(second)
                            .setScore(calculateSimilarity(first, second))
                            .setTimestamp(timestamp)
                            .build()
            );
        }
    }

    private float calculateSimilarity(Long first, Long second) {
        float commonSum = eventsMinWeightSum.get(first).get(second);
        float firstSum = (float) usersFeedback.get(first).values().stream().mapToDouble(e -> e).sum();
        float secondSum = (float) usersFeedback.get(second).values().stream().mapToDouble(e -> e).sum();
        float similarity = (float) (commonSum / (Math.sqrt(firstSum) * Math.sqrt(secondSum)));

        log.info("Определно сходство событий {} и {}: {}", first, second, similarity);
        return similarity;
    }

    private Float convertActionToWeight(ActionTypeAvro action) throws IncorrectActionTypeException {
        switch (action) {
            case VIEW -> {
                return  0.4F;
            }
            case REGISTER -> {
                return 0.8F;
            }
            case LIKE -> {
                return 1.0F;
            }
            default -> {
                log.warn("Неверный тип действия пользователя: {}", action);
                throw new IncorrectActionTypeException("Неверный тип действия пользователя: " + action);
            }
        }
    }
}
