package ru.practicum.request.dto;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.request.model.EventRequest;

@Component
@AllArgsConstructor
public class EventRequestMapper {
    public EventRequestDto mapRequest(EventRequest request) {
        EventRequestDto dto = new EventRequestDto();
        dto.setId(request.getId());
        dto.setRequester(request.getRequester().getId());
        dto.setEvent(request.getEvent().getId());
        dto.setCreated(request.getCreated());
		dto.setStatus(request.getStatus());
        return dto;
    }
}
