package ru.practicum.stats.analyzer.config;

import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.VoidDeserializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import ru.practicum.ewm.stats.avro.EventSimilarityAvro;
import ru.practicum.stats.analyzer.handler.SimilarityHandler;
import ru.practicum.stats.analyzer.kafka.SimilarityDeserializer;
import ru.practicum.stats.analyzer.starter.AnalyzerStarter;

import java.util.List;
import java.util.Properties;

@ConfigurationProperties("kafka.constants")
@AllArgsConstructor
public class KafkaConfig {
    private final String url;
    private final String action;
    private final String similarity;

    @Bean
    public AnalyzerStarter analyzerStarter(SimilarityHandler handler) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "action");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, url);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, VoidDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SimilarityDeserializer.class);

        Consumer<String, EventSimilarityAvro> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(List.of(similarity));

        return new AnalyzerStarter(consumer, handler);
    }
}
