package ru.practicum.request.service;

import ru.practicum.api.dto.request.EventRequestDto;
import ru.practicum.api.exception.ConflictException;
import ru.practicum.api.exception.NotFoundException;
import ru.practicum.api.exception.ValidationException;

import java.util.List;

public interface EventRequestService {

    EventRequestDto addRequest(Long userId, Long eventId) throws ConflictException, NotFoundException;

    List<EventRequestDto> getUserRequests(Long userId) throws NotFoundException;

    List<EventRequestDto> getRequestsByEventId(Long userId, Long eventId) throws ValidationException, NotFoundException;

    EventRequestDto updateRequest(Long userId,
                                  Long eventId,
                                  EventRequestDto request) throws ConflictException, ValidationException, NotFoundException;

    EventRequestDto cancelRequest(Long userId, Long requestId) throws NotFoundException, ValidationException;

    Long countByEventAndStatuses(Long eventId, List<String> statuses);

    List<EventRequestDto> getByEventAndStatus(List<Long> eventId, String status);

    List<EventRequestDto> findByEventIds(List<Long> id);
}
