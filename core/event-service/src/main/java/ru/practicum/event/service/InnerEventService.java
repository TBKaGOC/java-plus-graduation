package ru.practicum.event.service;

import ru.practicum.api.dto.event.EventFullDto;
import ru.practicum.api.dto.event.EventShortDto;

import java.util.List;

public interface InnerEventService {
    EventFullDto getEventById(Long eventId);

    boolean existsById(Long eventId);

    boolean existsByCategoryId(Long categoryId);

    List<EventShortDto> getShortByIds(List<Long> ids);
}
