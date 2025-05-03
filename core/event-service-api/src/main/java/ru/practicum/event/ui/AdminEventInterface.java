package ru.practicum.event.ui;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api.dto.event.EventFullDto;
import ru.practicum.api.exception.ConflictException;
import ru.practicum.api.exception.NotFoundException;
import ru.practicum.api.exception.ValidationException;
import ru.practicum.api.exception.WrongDataException;
import ru.practicum.api.request.event.UpdateEventAdminRequest;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.api.util.JsonFormatPattern.JSON_FORMAT_PATTERN_FOR_TIME;

@RequestMapping("/admin/events")
public interface AdminEventInterface {
    @GetMapping
    List<EventFullDto> getEvents(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Long> categories,
            @DateTimeFormat(pattern = JSON_FORMAT_PATTERN_FOR_TIME) @RequestParam(required = false) LocalDateTime rangeStart,
            @DateTimeFormat(pattern = JSON_FORMAT_PATTERN_FOR_TIME) @RequestParam(required = false) LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) throws ValidationException;

    @PatchMapping("/{eventId}")
    EventFullDto updateEvent(@PathVariable Long eventId,
                             @Valid @RequestBody UpdateEventAdminRequest event) throws ValidationException, ConflictException, WrongDataException, NotFoundException;
}
