package ru.practicum.event.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryMapper;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dto.UserMapper;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminEventServiceImpl implements AdminEventService {
    final EventRepository repository;
    final UserRepository userRepository;
    final CategoryRepository categoryRepository;

    @Override
    public List<EventFullDto> getEvents(List<Long> users, List<String> states, List<Long> categories, String rangeStart, String rangeEnd, Integer from, Integer size) {
        if (users.isEmpty()) {
            users = userRepository.findAllId();
        }
        if (categories.isEmpty()) {
            categories = categoryRepository.findAllId();
        }
        if (states.isEmpty()) {
            states = EventState.getAll();
        }

        if (rangeEnd == null) {
            List<EventFullDto> collect = repository.findByParametersWithoutEnd(users, states, categories, rangeStart, size + from)
                    .subList(from, size + from)
                    .stream()
                    .map((Event event) -> EventMapper.mapEventToFullDto(event, )
                    .collect(Collectors.toList());
            return collect
        }
    }

    @Override
    public EventFullDto updateEvent(Long eventId, EventFullDto event) {
        Event oldEvent = repository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Искомое событие не найдено " + eventId)
        );

        if (event.getState() != null &&
            event.getState().equals("PUBLISHED") &&
            oldEvent.getState() != EventState.PENDING) {
            throw new ConflictException("Невозможно опубликовать событие " + eventId);
        }

        if (event.getState() != null &&
            event.getState().equals("CANCELED") &&
            oldEvent.getState() != EventState.PENDING) {
            throw new ConflictException("Невозможно отменить событие " + eventId);
        }
        if (ChronoUnit.HOURS.between(oldEvent.getPublishedOn(), oldEvent.getEventDate()) < 1) {
            throw new ConflictException("Невозможно изменить событие" + eventId);
        }

        Event newEvent = new Event();
        newEvent.setId(eventId);
        newEvent.setAnnotation(event.getAnnotation() != null ? event.getAnnotation() : oldEvent.getAnnotation());
        newEvent.setCategory(event.getCategory() != null ?
                CategoryMapper.mapCategoryDto(event.getCategory()) : oldEvent.getCategory());
        newEvent.setCreatedOn(event.getCreatedOn() != null ?
                LocalDateTime.parse(event.getCreatedOn()) : oldEvent.getCreatedOn());
        newEvent.setDescription(event.getDescription() != null ? event.getDescription() : oldEvent.getDescription());
        newEvent.setEventDate(event.getEventDate() != null ? event.getEventDate() : oldEvent.getEventDate());
        newEvent.setInitiator(event.getInitiator() != null ?
                UserMapper.mapUserDto(event.getInitiator()) : oldEvent.getInitiator());
        newEvent.setLocation(event.getLocation() != null ? event.getLocation() : oldEvent.getLocation());
        newEvent.setPaid(event.getPaid() != null ? event.getPaid() : oldEvent.getPaid());
        newEvent.setParticipantLimit(event.getParticipantLimit() != null ?
                event.getParticipantLimit() : oldEvent.getParticipantLimit());
        newEvent.setPublishedOn(event.getPublishedOn() != null ?
                LocalDateTime.parse(event.getPublishedOn()) : oldEvent.getPublishedOn());
        newEvent.setRequestModeration(event.getRequestModeration() != null ?
                event.getRequestModeration() : oldEvent.getRequestModeration());
        newEvent.setState(event.getState() != null ? EventState.valueOf(event.getState()) : oldEvent.getState());
        newEvent.setTitle(event.getTitle() != null ? event.getTitle() : oldEvent.getTitle());

        repository.saveLocation(newEvent.getLocation().getLat(), newEvent.getLocation().getLon());

        return EventMapper.mapEventToFullDto(repository.save(newEvent), event.getConfirmedRequests());
    }
}
