package ru.practicum.application.event.ui;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.application.api.dto.event.EventFullDto;
import ru.practicum.application.api.dto.event.EventShortDto;

import java.util.List;

@RequestMapping("inner/event")
public interface InnerEventInterface {
    @GetMapping("/{eventId}")
    EventFullDto getEventById(@PathVariable Long eventId);

    @GetMapping("/{eventId}/exist")
    boolean existsById(@PathVariable Long eventId);

    @GetMapping("/category/{categoryId}/exist")
    boolean existsByCategoryId(@PathVariable Long categoryId);

    @GetMapping("/short/ids")
    List<EventShortDto> getShortByIds(@RequestBody List<Long> ids);
}
