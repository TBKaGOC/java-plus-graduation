package ru.practicum.event.controller;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.api.dto.event.EventFullDto;
import ru.practicum.api.dto.event.EventShortDto;
import ru.practicum.event.service.InnerEventService;
import ru.practicum.event.ui.InnerEventInterface;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InnerEventController implements InnerEventInterface {

    final InnerEventService innerEventService;

    @Override
    public EventFullDto getEventById(Long eventId) {
        return innerEventService.getEventById(eventId);
    }

    @Override
    public boolean existsById(Long eventId) {
        return innerEventService.existsById(eventId);
    }

    @Override
    public boolean existsByCategoryId(Long categoryId) {
        return false;
    }

    @Override
    public List<EventShortDto> getShortByIds(List<Long> ids) {
        return innerEventService.getShortByIds(ids);
    }
}
