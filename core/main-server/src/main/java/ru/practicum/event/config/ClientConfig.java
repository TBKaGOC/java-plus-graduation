package ru.practicum.event.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestClient;
import ru.practicum.client.StatsClient;

import java.net.URI;

@ConfigurationProperties("stats-server")
@AllArgsConstructor
public class ClientConfig {
    private final String url;

    @Bean
    private StatsClient statsClient(@Autowired RestClient.Builder builder) {
        return new StatsClient(builder, url);
    }

    @Bean
    @LoadBalanced
    @Primary
    private RestClient.Builder restTemplate() {
        return RestClient.builder();
    }
}
