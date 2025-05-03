package ru.practicum.application.event.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.application.event.ui.EventInterface;

@FeignClient(name = "events-management")
public interface EventClient extends EventInterface {
}
