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
    final Map<Long, Map<Long, Double>>  usersFeedback;
    // Map<Event, Map<Event, MinSum>>
    final Map<Long, Map<Long, Double>> eventsMinWeightSum;
    // Map<Event, SumWeight>;
    final Map<Long, Double> eventWeightSum;
    final Map<Long, Map<Long, Double>> eventsSimilarity;


    @Autowired
    public UserActionHandler(SimilarityProducer producer) {
        this.producer = producer;
        usersFeedback = new HashMap<>();
        eventsMinWeightSum = new HashMap<>();
        eventWeightSum = new HashMap<>();
        eventsSimilarity = new HashMap<>();
    }

    public void handle(UserActionAvro avro) throws IncorrectActionTypeException {
        Long userId = avro.getUserId();
        Long eventId = avro.getEventId();
        Double weight = convertActionToWeight(avro.getActionType());
        Map<Long, Double> userRatings = new HashMap<>(usersFeedback.computeIfAbsent(eventId, key -> Map.of(userId, weight)));

        if (!userRatings.containsKey(userId) || userRatings.get(userId) < weight) {
            Double oldWeight = userRatings.getOrDefault(userId, 0.0);
            userRatings.put(userId, weight);
            usersFeedback.put(eventId, userRatings);
            determineSimilarity(eventId, userId, oldWeight, weight, avro.getTimestamp());
        } else if (userRatings.size() == 1) {
            determineSimilarity(eventId, userId, 0.0, weight, avro.getTimestamp());
        }
    }

    public void flush() {
        producer.flush();
    }

    public void close() {
        producer.close();
    }

    private void determineSimilarity(Long eventId, Long userId, Double oldWeight, Double newWeight, Instant timestamp) {
        eventWeightSum.put(eventId, eventWeightSum.getOrDefault(eventId, 0.0) - oldWeight + newWeight);
        List<Long> eventsWithSameUsers = usersFeedback.keySet().stream()
                .filter(e -> usersFeedback.get(e).containsKey(userId) && !Objects.equals(e, eventId))
                .toList();

        for (Long convergenceEvent: eventsWithSameUsers) {
            Double convergenceWeight = usersFeedback.getOrDefault(convergenceEvent, new HashMap<>())
                    .getOrDefault(userId, 0.0);
            Long first = Math.min(eventId, convergenceEvent);
            Long second = Math.max(eventId, convergenceEvent);
            Double oldSum = eventsMinWeightSum.getOrDefault(first, new HashMap<>()).getOrDefault(second, 0.0);
            Double newSum = oldSum - Math.min(oldWeight, convergenceWeight) + Math.min(newWeight, convergenceWeight);
            Map<Long, Double> userRating = new HashMap<>(eventsMinWeightSum.getOrDefault(first, new HashMap<>()));
            userRating.put(second, newSum);
            eventsMinWeightSum.put(first, userRating);
            EventSimilarityAvro eventSimilarityAvro = EventSimilarityAvro.newBuilder()
                    .setEventA(first)
                    .setEventB(second)
                    .setScore(calculateSimilarity(first, second, newSum))
                    .setTimestamp(timestamp)
                    .build();

            if (eventsSimilarity.getOrDefault(first, new HashMap<>()).getOrDefault(second, -1.0) !=
                    eventSimilarityAvro.getScore()) {
                Map<Long, Double> inner = eventsSimilarity.getOrDefault(first, new HashMap<>());
                inner.put(second, eventSimilarityAvro.getScore());
                eventsSimilarity.put(first, inner);
                producer.sendMessage(eventSimilarityAvro);
            }
        }
    }

    private double calculateSimilarity(Long first, Long second, Double commonSum) {
        double firstSum = eventWeightSum.get(first);
        double secondSum = eventWeightSum.get(second);
        double similarity = commonSum / (Math.sqrt(firstSum) * Math.sqrt(secondSum));

        log.info("Определно сходство событий {} и {}: {}", first, second, similarity);
        return similarity;
    }

    private Double convertActionToWeight(ActionTypeAvro action) throws IncorrectActionTypeException {
        switch (action) {
            case VIEW -> {
                return  0.4;
            }
            case REGISTER -> {
                return 0.8;
            }
            case LIKE -> {
                return 1.0;
            }
            default -> {
                log.warn("Неверный тип действия пользователя: {}", action);
                throw new IncorrectActionTypeException("Неверный тип действия пользователя: " + action);
            }
        }
    }
}
