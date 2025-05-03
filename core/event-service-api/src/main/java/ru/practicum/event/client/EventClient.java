package ru.practicum.event.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.event.ui.EventInterface;

@FeignClient(name = "events-management")
public interface EventClient extends EventInterface {
}
