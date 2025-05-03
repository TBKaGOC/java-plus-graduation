package ru.practicum.event.service;

import ru.practicum.api.dto.event.EventFullDto;
import ru.practicum.api.dto.event.EventShortDto;
import ru.practicum.api.exception.NotFoundException;
import ru.practicum.api.exception.ValidationException;

import java.util.List;

public interface EventService {
    EventFullDto getEventById(Long eventId, String uri, String ip) throws NotFoundException;

    List<EventShortDto> getFilteredEvents(String text,
                                          List<Long> categories,
                                          Boolean paid,
                                          String rangeStart,
                                          String rangeEnd,
                                          Boolean onlyAvailable,
                                          String sort,
                                          Integer from,
                                          Integer size,
                                          String uri,
                                          String ip) throws ValidationException;
}
