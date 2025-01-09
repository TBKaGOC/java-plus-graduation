package ru.practicum.event.service;

import ru.practicum.event.dto.EventFullDto;

import java.util.List;

public interface AdminEventService {

    List<EventFullDto> getEvents(List<Long> users, List<String> states, List<Long> categories, String rangeStart, String rangeEnd, Integer from, Integer size);

    EventFullDto updateEvent(Long eventId, EventFullDto event);

}
