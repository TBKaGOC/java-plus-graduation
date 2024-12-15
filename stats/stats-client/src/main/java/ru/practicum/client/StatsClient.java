package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.practicum.dto.StatsRequestDto;
import ru.practicum.dto.StatsResponseDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;


@RequiredArgsConstructor
@Component
public class StatsClient {
    private final RestClient client;

    public Collection<StatsResponseDto> getAllStats(LocalDateTime start, LocalDateTime end, ArrayList<String> uris, Boolean unique) {
        return Arrays.asList(Objects.requireNonNull(client.get()
                .uri("/stats?start={start}&end={end}&uris={uris}&unique={unique}")
                .retrieve()
                .body(StatsResponseDto[].class)));
    }

    public StatsResponseDto postStats(StatsRequestDto statsRequestDto) {
        return client.post()
                .uri("/hit")
                .contentType(MediaType.APPLICATION_JSON)
                .body(statsRequestDto)
                .retrieve()
                .body(StatsResponseDto.class);
    }
}
