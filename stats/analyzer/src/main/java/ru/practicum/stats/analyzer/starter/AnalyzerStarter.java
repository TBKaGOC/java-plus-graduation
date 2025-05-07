package ru.practicum.stats.analyzer.starter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.stats.avro.EventSimilarityAvro;
import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.practicum.stats.analyzer.handler.SimilarityHandler;

import java.time.Duration;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
@Slf4j
public class AnalyzerStarter {
    final Consumer<String, EventSimilarityAvro> consumer;
    final SimilarityHandler handler;

    public AnalyzerStarter(Consumer<String, EventSimilarityAvro> consumer, SimilarityHandler handler) {
        this.consumer = consumer;
        this.handler = handler;
    }

    public void start() {
        try {
            log.info("Получение данных");
            while (true) {
                ConsumerRecords<String, EventSimilarityAvro> records = consumer.poll(Duration.ofMillis(500));

                for (ConsumerRecord<String, EventSimilarityAvro> record : records) {
                    System.out.println(record.value());
                }
            }
        } catch (WakeupException e) {

        } catch (Exception e) {
            log.error("Сбой обработки ", e);
            log.error(e.getMessage());
        } finally {
            try {
                consumer.commitSync();
            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
                log.info("Закрываем продюсер");
            }
        }
    }
}
