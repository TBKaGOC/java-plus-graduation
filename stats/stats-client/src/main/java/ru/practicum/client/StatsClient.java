package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;


@RequiredArgsConstructor
public class StatsClient {
    private final RestClient client;

    public void getAllStats(LocalDateTime start, LocalDateTime end, ArrayList<String> uris, Boolean unique) {
        client.get()
                .uri(UriComponentsBuilder.fromUriString("/stats?start={start}&end={end}&uris={uris}&unique={unique}")
                        .build(Map.of("start", start,
                                "end", end,
                                "uris", uris,
                                "unique", unique)
                        )
                )
                .retrieve()
                .body(/*Класс дто-шки*/);
    }

    public void postStats(/*Дто-шка*/) {
        client.post()
                .uri(UriComponentsBuilder.fromUriString("/hit").build().toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .body(/*Дто-шка*/)
                .retrieve()
                .body(/*Класс дто-шки*/);
    }
}
