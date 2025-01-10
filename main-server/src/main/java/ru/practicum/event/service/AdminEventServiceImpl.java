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
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.StateAction;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.repository.LocationRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.exception.WrongDataException;
import ru.practicum.request.model.EventRequest;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.util.JsonFormatPattern.JSON_FORMAT_PATTERN_FOR_TIME;

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
    public EventFullDto updateEvent(Long eventId, UpdateEventUserRequest eventDto) {
        log.info("Users...");
        log.info("Редактирование данных события и его статуса");
        Event event = getEventById(eventId);
        validationEventDate(event);

        if (!event.getState().equals(EventState.PENDING)) {
            throw new ConflictException("Событие не в состоянии \"Ожидание публикации\", изменение события невозможно");
        }
        if ((!StateAction.REJECT_EVENT.toString().equals(eventDto.getStateAction())
                && event.getState().equals(EventState.PUBLISHED))) {
            throw new ConflictException("Отклонить опубликованное событие невозможно");
        }

        updateEventFromEventDto(event, eventDto);
        locationRepository.save(event.getLocation());
        eventRepository.save(event);
        Long confirmed = requestRepository.countByEventAndStatuses(event.getId(), List.of("CONFIRMED"));
        return getViewsCounter(EventMapper.mapEventToFullDto(event, confirmed));
    }

    Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Событие " + eventId + " не найдено"));
    }

    private static void validationEventDate(Event event) {
        if (LocalDateTime.now().isAfter(event.getEventDate().minusHours(1))) {
            throw new ValidationException("До начала события меньше часа, изменение невозможно");
        }
        if (event.getEventDate().isBefore(LocalDateTime.now())) {
            throw new WrongDataException("Событие уже завершилось");
        }
    }

    void updateEventFromEventDto(Event event, UpdateEventUserRequest inpEventDto) {
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
            event.setEventDate(LocalDateTime.parse(inpEventDto.getEventDate(), DateTimeFormatter.ofPattern(JSON_FORMAT_PATTERN_FOR_TIME)));
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
        if (inpEventDto.getStateAction() != null) {
            switch (inpEventDto.getStateAction().toUpperCase()) {
                case "PUBLISH_EVENT":
                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
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
    }

    EventFullDto getViewsCounter(EventFullDto eventFullDto) {
        ArrayList<String> urls = new ArrayList<>(List.of("/events/" + eventFullDto.getId()));
        LocalDateTime start = LocalDateTime.parse(eventFullDto.getCreatedOn(), DateTimeFormatter.ofPattern(JSON_FORMAT_PATTERN_FOR_TIME));
        LocalDateTime end = LocalDateTime.now();

        Integer views = statsClient.getAllStats(start, end, urls, true).size();
        eventFullDto.setViews(views);
        return eventFullDto;
    }

}
