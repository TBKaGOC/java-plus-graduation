package ru.practicum.application.request.ui;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.application.api.exception.ConflictException;
import ru.practicum.application.api.exception.NotFoundException;
import ru.practicum.application.api.exception.ValidationException;
import ru.practicum.application.api.dto.request.EventRequestDto;

import java.util.List;

@RequestMapping("/users/{userId}")
@RestController
public interface EventRequestInterface {
    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    EventRequestDto addEventRequest(
            @PathVariable Long userId,
            @RequestParam Long eventId
    ) throws ConflictException, NotFoundException;

    @GetMapping("/requests")
    List<EventRequestDto> getUserRequests(@PathVariable Long userId) throws NotFoundException;

    @GetMapping("/events/{eventId}/requests")
    List<EventRequestDto> getRequestsByEventId(
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) throws ValidationException, NotFoundException;

    @PatchMapping("/events/{eventId}/requests")
    EventRequestDto updateRequest(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody EventRequestDto request
    ) throws ValidationException, ConflictException, NotFoundException;

    @PatchMapping("/requests/{requestId}/cancel")
    EventRequestDto cancelRequest(
            @PathVariable Long userId,
            @PathVariable Long requestId
    ) throws ValidationException, NotFoundException;
}
