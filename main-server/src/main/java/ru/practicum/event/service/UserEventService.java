package ru.practicum.event.service;

import ru.practicum.event.dto.*;

import java.util.List;

public interface UserEventService {

    EventFullDto addEvent(Long userId, NewEventDto event);

    EventFullDto updateEvent(Long userId, Long eventId, NewEventDto event);

    List<EventShortDto> getUserEvents(Long userId, Integer from, Integer count);

    EventFullDto getEventById(Long userId, Long eventId);
}
