package ru.practicum.event.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.client.StatsClient;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventMapper;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEventServiceImpl implements UserEventService {

    final EventRepository eventRepository;
    final UserRepository userRepository;
    final CategoryRepository categoryRepository;
    final RequestRepository requestRepository;

    final StatsClient statsClient;

    @Override
    public EventFullDto addEvent(Long userId, NewEventDto eventDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        Category category = categoryRepository.findById(eventDto.getCategory()).orElseThrow(
                () -> new NotFoundException("Категория не найдена " + eventDto.getCategory())
        );

        Event event = EventMapper.mapNewEventDtoToEvent(eventDto, category);

        event.setInitiator(user);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);

        if (event.getPaid() == null) {
            event.setPaid(false);
        }
        if (event.getParticipantLimit() == null) {
            event.setParticipantLimit(0);
        }
        if (event.getRequestModeration() == null) {
            event.setRequestModeration(true);
        }
        if (LocalDateTime.now().isAfter(event.getEventDate().minusHours(1))) {
            throw new ValidationException("До начала события меньше часа, изменение невозможно");
        }

        eventRepository.saveLocation(event.getLocation().getLat(), event.getLocation().getLon());
        event = eventRepository.save(event);

        Long confirmedRequests = requestRepository.countByEventAndStatuses(event.getId(), List.of("CONFIRMED"));
        return EventMapper.mapEventToFullDto(event, confirmedRequests);
    }

    @Override
    public EventFullDto updateEvent(Long userId, Long eventId, NewEventDto eventDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        Event event = getEventById(eventId);
        if (!user.getId().equals(event.getInitiator().getId())) {
            throw new ValidationException("Пользователь " + userId + " не инициатор события " + eventId);
        }
        event = updateEventFromEventDto(event, eventDto);
        eventRepository.saveLocation(event.getLocation().getLat(), event.getLocation().getLon());
        eventRepository.save(event);

        Long confirmed = requestRepository.countByEventAndStatuses(event.getId(), List.of("CONFIRMED"));
        return getViewsCounter(EventMapper.mapEventToFullDto(event, confirmed));
    }

    @Override
    public List<EventShortDto> getUserEvents(Long userId, Integer from, Integer count) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        return eventRepository.findAllByInitiator(user, PageRequest.of(from / count, count)).stream()
                .map(EventMapper::mapEventToShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventById(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        Event event = getEventById(eventId);
        if (!user.getId().equals(event.getInitiator().getId())) {
            throw new ValidationException("Пользователь " + userId + " не является инициатором события " + eventId);
        }
        Long confirmed = requestRepository.countByEventAndStatuses(event.getId(), List.of("CONFIRMED"));
        return getViewsCounter(EventMapper.mapEventToFullDto(event, confirmed));
    }

    // Вспомогательные функции

    Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Событие " + eventId + " не найдено"));
    }

    Event updateEventFromEventDto(Event event, NewEventDto inpEventDto) {
        if (inpEventDto.getAnnotation() != null) {
            event.setAnnotation(inpEventDto.getAnnotation());
        }
        if (inpEventDto.getCategory() != null) {
            Category category = categoryRepository.findById(inpEventDto.getCategory()).orElseThrow(
                    () -> new NotFoundException("Категория не найдена " + inpEventDto.getCategory()));
            event.setCategory(category);
        }
        if (inpEventDto.getDescription() != null) {
            event.setDescription(inpEventDto.getDescription());
        }
        if (inpEventDto.getEventDate() != null) {
            event.setEventDate(inpEventDto.getEventDate());
        }
        if (inpEventDto.getLocation() != null) {
            event.setLocation(inpEventDto.getLocation());
        }
        if (inpEventDto.getPaid() != null) {
            event.setPaid(inpEventDto.getPaid());
        }
        if (inpEventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(inpEventDto.getParticipantLimit());
        }
        if (inpEventDto.getRequestModeration() != null) {
            event.setRequestModeration(inpEventDto.getRequestModeration());
        }
        if (inpEventDto.getState() != null) {
            switch (inpEventDto.getState().toUpperCase()) {
                case "PUBLISH_EVENT":
                    event.setState(EventState.PUBLISHED);
                    break;
                case "REJECT_EVENT":
                case "CANCEL_REVIEW":
                    event.setState(EventState.CANCELED);
                    break;
                case "SEND_TO_REVIEW":
                    event.setState(EventState.PENDING);
                    break;
                default:
                    throw new ValidationException("Неверное состояние события, не удалось обновить");
            }
        }
        if (LocalDateTime.now().isAfter(event.getEventDate().minusHours(2))) {
            throw new ValidationException("До начала события осталось меньше 2 часов " + event.getId());
        }
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ValidationException("Нельзя изменить опубликованное событие " + event.getId());
        }
        return event;
    }

    EventFullDto getViewsCounter(EventFullDto eventFullDto) {
        ArrayList<String> urls = new ArrayList<>(List.of("/events/" + eventFullDto.getId()));
        LocalDateTime start = LocalDateTime.parse(eventFullDto.getCreatedOn(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime end = LocalDateTime.now();

        Integer views = statsClient.getAllStats(start, end, urls, true).size();
        eventFullDto.setViews(views);
        return eventFullDto;
    }
}
