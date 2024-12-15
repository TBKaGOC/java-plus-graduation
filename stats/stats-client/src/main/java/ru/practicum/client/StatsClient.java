package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.dto.StatsRequestDto;
import ru.practicum.dto.StatsResponseDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;


@RequiredArgsConstructor
public class StatsClient {
    private final RestClient client;

    public RestClient.ResponseSpec getAllStats(LocalDateTime start, LocalDateTime end, ArrayList<String> uris, Boolean unique) {
        return client.get()
                .uri(UriComponentsBuilder.fromUriString("/stats?start={start}&end={end}&uris={uris}&unique={unique}")
                        .build(Map.of("start", start,
                                "end", end,
                                "uris", uris,
                                "unique", unique)
                        )
                )
                .retrieve();
    }

    public StatsResponseDto postStats(StatsRequestDto statsRequestDto) {
        return client.post()
                .uri(UriComponentsBuilder.fromUriString("/hit").build().toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .body(statsRequestDto)
                .retrieve()
                .body(StatsResponseDto.class);
    }
}
