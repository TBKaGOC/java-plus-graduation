package ru.practicum.event.service;

import ru.practicum.api.dto.event.EventFullDto;
import ru.practicum.api.dto.event.EventShortDto;
import ru.practicum.api.dto.event.NewEventDto;
import ru.practicum.api.exception.ConflictException;
import ru.practicum.api.exception.NotFoundException;
import ru.practicum.api.exception.ValidationException;
import ru.practicum.api.exception.WrongDataException;
import ru.practicum.api.request.event.UpdateEventUserRequest;

import java.util.List;

public interface UserEventService {

    EventFullDto addEvent(Long userId, NewEventDto event) throws ValidationException, WrongDataException, NotFoundException;

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest event) throws ConflictException, NotFoundException, ValidationException, WrongDataException;

    List<EventShortDto> getUserEvents(Long userId, Integer from, Integer count) throws NotFoundException;

    EventFullDto getEventById(Long userId, Long eventId) throws NotFoundException, ValidationException;
}
