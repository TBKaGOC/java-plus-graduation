package ru.practicum.application.request.ui;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.application.api.dto.request.EventRequestDto;

import java.util.List;

@RequestMapping("/inner/request")
public interface InnerEventRequestInterface {
    @GetMapping("/{eventId}/status/count")
    Long countByEventAndStatuses(@PathVariable Long eventId, @RequestBody List<String> statuses);
    @GetMapping("/events/{status}")
    List<EventRequestDto> getByEventAndStatus(@RequestBody List<Long> eventId, @PathVariable String status);

    @GetMapping("/events")
    List<EventRequestDto> findByEventIds(@RequestBody List<Long> id);
}
