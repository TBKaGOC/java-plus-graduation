package ru.practicum.event.service;

import jakarta.ws.rs.NotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.api.dto.category.CategoryDto;
import ru.practicum.api.dto.event.EventFullDto;
import ru.practicum.api.dto.event.EventShortDto;
import ru.practicum.api.dto.user.UserDto;
import ru.practicum.category.client.InnerCategoryClient;
import ru.practicum.category.client.PublicCategoryClient;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.user.client.UserClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InnerEventServiceImpl implements InnerEventService {
    final EventRepository eventRepository;

    final UserClient userClient;
    final InnerCategoryClient categoryClient;

    @Override
    public EventFullDto getEventById(Long eventId) {
        return EventMapper.mapEventToFullDto(eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Не найдено событие " + eventId)
        ), null, null, null);
    }

    @Override
    public boolean existsById(Long eventId) {
        return eventRepository.existsById(eventId);
    }

    @Override
    public boolean existsByCategoryId(Long categoryId) {
        return eventRepository.existsByCategoryId(categoryId);
    }

    @Override
    public List<EventShortDto> getShortByIds(List<Long> ids) {
        List<Event> events = eventRepository.findAllById(ids);
        Map<Long, UserDto> users = userClient.getUsersList(
                events.stream().map(Event::getInitiator).collect(Collectors.toList()), 0, events.size()
        ).stream().collect(Collectors.toMap(UserDto::getId, userDto -> userDto));
        Map<Long, CategoryDto> categories = categoryClient.getCategoriesByIds(
                events.stream().map(Event::getCategoryId).collect(Collectors.toSet())
        ).stream().collect(Collectors.toMap(CategoryDto::getId, categoryDto -> categoryDto));

        return events.stream().map(
                e -> EventMapper.mapEventToShortDto(e, categories.get(e.getCategoryId()), users.get(e.getInitiator()))
        ).collect(Collectors.toList());
    }
}
