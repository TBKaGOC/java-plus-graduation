package ru.practicum.service;

import ru.practicum.StatsRequestDto;
import ru.practicum.StatsResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    StatsRequestDto save(StatsRequestDto requestDto);

    List<StatsResponseDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);

}
