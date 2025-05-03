package ru.practicum.application.event.ui;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.application.api.dto.event.EventFullDto;
import ru.practicum.application.api.dto.event.EventShortDto;
import ru.practicum.application.api.dto.event.NewEventDto;
import ru.practicum.application.api.request.event.UpdateEventUserRequest;
import ru.practicum.application.api.exception.ConflictException;
import ru.practicum.application.api.exception.NotFoundException;
import ru.practicum.application.api.exception.ValidationException;
import ru.practicum.application.api.exception.WrongDataException;

import java.util.List;

@RequestMapping("/users/{userId}/events")
public interface UserEventInterface {
    @GetMapping
    List<EventShortDto> getUserEvents(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer count
    ) throws NotFoundException;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    EventFullDto addEvent(
            @PathVariable Long userId,
            @Valid @RequestBody NewEventDto event
    ) throws ValidationException, WrongDataException, NotFoundException;

    @GetMapping("/{eventId}")
    EventFullDto getEventById(
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) throws ValidationException, NotFoundException;

    @PatchMapping("/{eventId}")
    EventFullDto updateEvent(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @Valid @RequestBody UpdateEventUserRequest event
    ) throws ValidationException, ConflictException, WrongDataException, NotFoundException;
}
