package ru.practicum.application.event.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.application.api.dto.category.CategoryDto;
import ru.practicum.application.api.dto.event.EventFullDto;
import ru.practicum.application.api.dto.event.EventShortDto;
import ru.practicum.application.api.dto.user.UserDto;
import ru.practicum.application.api.exception.NotFoundException;
import ru.practicum.application.category.client.CategoryClient;
import ru.practicum.application.event.repository.EventRepository;
import ru.practicum.application.event.mapper.EventMapper;
import ru.practicum.application.event.model.Event;
import ru.practicum.application.user.client.UserClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InnerEventServiceImpl implements InnerEventService {
    final EventRepository eventRepository;

    final UserClient userClient;
    final CategoryClient categoryClient;

    @Override
    public EventFullDto getEventById(Long eventId) throws NotFoundException {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Не найдено событие " + eventId)
        );
        return EventMapper.mapEventToFullDto(event, null, categoryClient.getCategoryById(event.getCategory()),
                userClient.getById(event.getInitiator()));
    }

    @Override
    public boolean existsById(Long eventId) {
        return eventRepository.existsById(eventId);
    }

    @Override
    public boolean existsByCategoryId(Long categoryId) {
        return eventRepository.existsByCategory(categoryId);
    }

    @Override
    public List<EventShortDto> getShortByIds(List<Long> ids) {
        List<Event> events = eventRepository.findAllById(ids);
        Map<Long, UserDto> users = userClient.getUsersList(
                events.stream().map(Event::getInitiator).collect(Collectors.toList()), 0, events.size()
        ).stream().collect(Collectors.toMap(UserDto::getId, userDto -> userDto));
        Map<Long, CategoryDto> categories = categoryClient.getCategoriesByIds(
                events.stream().map(Event::getCategory).collect(Collectors.toSet())
        ).stream().collect(Collectors.toMap(CategoryDto::getId, categoryDto -> categoryDto));

        return events.stream().map(
                e -> EventMapper.mapEventToShortDto(e, categories.get(e.getCategory()), users.get(e.getInitiator()))
        ).collect(Collectors.toList());
    }
}
