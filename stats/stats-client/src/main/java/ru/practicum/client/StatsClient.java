package ru.practicum.client;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.dto.StatsRequestDto;
import ru.practicum.dto.StatsResponseDto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatsClient {
    final RestClient client;
    final String baseUri;

    public StatsClient(RestClient.Builder rest, String baseUri) {
        this.client = rest.build();
        this.baseUri = baseUri;
    }

    public Collection<StatsResponseDto> getAllStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        String uri = UriComponentsBuilder.fromPath("/stats")
                .queryParam("start", start)
                .queryParam("end", end)
                .queryParam("uris", String.join(",", uris))
                .queryParam("unique", Optional.ofNullable(unique).orElse(false))
                .build()
                .toUriString();

        StatsResponseDto[] response = client.get()
                .uri(baseUri + uri)
                .retrieve()
                .body(StatsResponseDto[].class);

        // Возвращаем пустую коллекцию, если результат null
        return response != null ? Arrays.asList(response) : List.of();
    }

    public StatsResponseDto postStats(StatsRequestDto statsRequestDto) {
        return Optional.ofNullable(client.post()
                        .uri(baseUri + "/hit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(statsRequestDto)
                        .retrieve()
                        .body(StatsResponseDto.class))
                .orElseThrow(() -> new IllegalStateException("Failed to post stats: response is null"));
    }
}
