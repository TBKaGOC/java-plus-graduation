package ru.practicum.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import ru.practicum.client.StatsClient;

@Configuration
public class WebConfig {
    @Value("${stats-server.url}")
    private String serverUrl;

    public StatsClient statsClient() {
        return new StatsClient(RestClient.builder()
                .baseUrl(serverUrl)
                .build());
    }
}
