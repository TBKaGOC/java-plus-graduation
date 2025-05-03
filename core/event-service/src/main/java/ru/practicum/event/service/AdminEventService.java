package ru.practicum.event.service;

import ru.practicum.api.dto.event.EventFullDto;
import ru.practicum.api.exception.ConflictException;
import ru.practicum.api.exception.NotFoundException;
import ru.practicum.api.exception.ValidationException;
import ru.practicum.api.exception.WrongDataException;
import ru.practicum.api.request.event.UpdateEventAdminRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventService {

    List<EventFullDto> getEvents(List<Long> users, List<String> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) throws ValidationException;

    EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest event) throws ConflictException, ValidationException, NotFoundException, WrongDataException;

}
