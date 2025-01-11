package ru.practicum.event.service;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;

import java.util.List;

public interface UserEventService {

    EventFullDto addEvent(Long userId, NewEventDto event);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest event);

    List<EventShortDto> getUserEvents(Long userId, Integer from, Integer count);

    EventFullDto getEventById(Long userId, Long eventId);
}
