package ru.practicum.event.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.api.dto.event.EventFullDto;
import ru.practicum.api.dto.event.EventShortDto;
import ru.practicum.api.dto.event.NewEventDto;
import ru.practicum.api.exception.ConflictException;
import ru.practicum.api.exception.NotFoundException;
import ru.practicum.api.exception.ValidationException;
import ru.practicum.api.exception.WrongDataException;
import ru.practicum.api.request.event.UpdateEventUserRequest;
import ru.practicum.event.service.UserEventService;
import ru.practicum.event.ui.UserEventInterface;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEventController implements UserEventInterface {

    final UserEventService eventService;

    @Override
    public List<EventShortDto> getUserEvents(
            Long userId,
            Integer from,
            Integer count
    ) throws NotFoundException {
        return eventService.getUserEvents(userId, from, count);
    }

    @Override
    public EventFullDto addEvent(
            Long userId,
            NewEventDto event
    ) throws ValidationException, WrongDataException, NotFoundException {
        return eventService.addEvent(userId, event);
    }

    @Override
    public EventFullDto getEventById(
            Long userId,
            Long eventId
    ) throws ValidationException, NotFoundException {
        return eventService.getEventById(userId, eventId);
    }

    @Override
    public EventFullDto updateEvent(
            Long userId,
            Long eventId,
            UpdateEventUserRequest event
    ) throws ValidationException, ConflictException, WrongDataException, NotFoundException {
        return eventService.updateEvent(userId, eventId, event);
    }
}
