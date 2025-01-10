package ru.practicum.event.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryMapper;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.client.StatsClient;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.repository.LocationRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.request.model.EventRequest;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.dto.UserMapper;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminEventServiceImpl implements AdminEventService {
    final EventRepository eventRepository;
    final RequestRepository requestRepository;
    final UserRepository userRepository;
    final CategoryRepository categoryRepository;
    final LocationRepository locationRepository;

    final StatsClient statsClient;

    @Override
    public List<EventFullDto> getEvents(List<Long> users, List<String> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {

        List<EventFullDto> eventDtos = null;
        List<EventState> eventStateList = null;

        if (rangeStart != null && rangeEnd != null) {
            if (rangeStart.isAfter(rangeEnd)) {
                throw new ValidationException("Время начала поиска позже времени конца поиска");
            }
        }

        if ((users == null) || (users.isEmpty())) {
            users = userRepository.findAllId();
        }
        if ((categories == null) || (categories.isEmpty())) {
            categories = categoryRepository.findAllId();
        }
        if ((states == null) || (states.isEmpty())) {
            eventStateList = Arrays.stream(EventState.values()).collect(Collectors.toList());
        } else {
            eventStateList = states.stream().map(EventState::valueOf).collect(Collectors.toList());
        }

        List<Event> allEventsWithDates = new ArrayList<>(eventRepository.findAllEventsWithDates(users,
                eventStateList, categories, rangeStart, rangeEnd, PageRequest.of(from / size, size)));
        List<EventRequest> requestsByEventIds = requestRepository.findByEventIds(allEventsWithDates.stream()
                .mapToLong(Event::getId).boxed().collect(Collectors.toList()));
        eventDtos = allEventsWithDates.stream()
                .map(e -> EventMapper.mapEventToFullDto(e,
                        requestsByEventIds.stream()
                                .filter(r -> r.getEvent().getId().equals(e.getId()))
                                .count()))
                .toList();

        if (!eventDtos.isEmpty()) {
            HashMap<Long, Integer> eventIdsWithViewsCounter = new HashMap<>();
            LocalDateTime startTime = LocalDateTime.parse(eventDtos.get(0).getCreatedOn().replace(" ", "T"));
            ArrayList<String> uris = new ArrayList<>();
            for (EventFullDto dto : eventDtos) {
                eventIdsWithViewsCounter.put(dto.getId(), 0);
                uris.add("/events/" + dto.getId().toString());
                if (startTime.isAfter(LocalDateTime.parse(dto.getCreatedOn().replace(" ", "T")))) {
                    startTime = LocalDateTime.parse(dto.getCreatedOn().replace(" ", "T"));
                }
            }

            var viewsCounter = statsClient.getAllStats(startTime, LocalDateTime.now(), uris, true);
            for (var statsDto : viewsCounter) {
                String[] split = statsDto.getUri().split("/");
                eventIdsWithViewsCounter.put(Long.parseLong(split[2]), Math.toIntExact(statsDto.getHits()));
            }
            ArrayList<Long> longs = new ArrayList<>(eventIdsWithViewsCounter.keySet());
            List<EventRequest> requests = requestRepository.findByEventIdsAndStatus(longs, "CONFIRMED");
            return eventDtos.stream()
                    .peek(dto -> dto.setConfirmedRequests(
                            requests.stream()
                                    .filter((request -> request.getEvent().getId().equals(dto.getId())))
                                    .count()
                    ))
                    .peek(dto -> dto.setViews(eventIdsWithViewsCounter.get(dto.getId())))
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public EventFullDto updateEvent(Long eventId, EventFullDto event) {
        Event oldEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Искомое событие не найдено " + eventId));

        String newState = event.getState();
        if (newState != null) {
            if (newState.equals("PUBLISHED") && oldEvent.getState() != EventState.PENDING) {
                throw new ConflictException("Невозможно опубликовать событие " + eventId);
            }
            if (newState.equals("CANCELED") && oldEvent.getState() != EventState.PENDING) {
                throw new ConflictException("Невозможно отменить событие " + eventId);
            }
        }

        if (oldEvent.getState() == EventState.PUBLISHED &&
                ChronoUnit.HOURS.between(oldEvent.getPublishedOn(), oldEvent.getEventDate()) < 1) {
            throw new ConflictException("Невозможно изменить событие " + eventId +
                    ", так как разница между датой публикации и проведения менее 1 часа.");
        }

        Event newEvent = new Event();
        newEvent.setId(eventId);
        newEvent.setAnnotation(Optional.ofNullable(event.getAnnotation()).orElse(oldEvent.getAnnotation()));
        newEvent.setCategory(Optional.ofNullable(event.getCategory())
                .map(CategoryMapper::mapCategoryDto)
                .orElse(oldEvent.getCategory()));
        newEvent.setCreatedOn(Optional.ofNullable(event.getCreatedOn())
                .map(LocalDateTime::parse)
                .orElse(oldEvent.getCreatedOn()));
        newEvent.setDescription(Optional.ofNullable(event.getDescription()).orElse(oldEvent.getDescription()));
        newEvent.setEventDate(Optional.ofNullable(event.getEventDate()).orElse(oldEvent.getEventDate()));
        newEvent.setInitiator(Optional.ofNullable(event.getInitiator())
                .map(UserMapper::mapUserDto)
                .orElse(oldEvent.getInitiator()));
        newEvent.setLocation(Optional.ofNullable(event.getLocation()).orElse(oldEvent.getLocation()));
        newEvent.setPaid(Optional.ofNullable(event.getPaid()).orElse(oldEvent.getPaid()));
        newEvent.setParticipantLimit(Optional.ofNullable(event.getParticipantLimit()).orElse(oldEvent.getParticipantLimit()));
        newEvent.setPublishedOn(Optional.ofNullable(event.getPublishedOn())
                .map(LocalDateTime::parse)
                .orElse(oldEvent.getPublishedOn()));
        newEvent.setRequestModeration(Optional.ofNullable(event.getRequestModeration()).orElse(oldEvent.getRequestModeration()));
        newEvent.setState(Optional.ofNullable(newState).map(EventState::valueOf).orElse(oldEvent.getState()));
        newEvent.setTitle(Optional.ofNullable(event.getTitle()).orElse(oldEvent.getTitle()));

        locationRepository.save(newEvent.getLocation());

        return EventMapper.mapEventToFullDto(eventRepository.save(newEvent), event.getConfirmedRequests());
    }

}
