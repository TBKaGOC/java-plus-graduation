package ru.practicum.application.event.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.application.event.ui.UserEventInterface;

@FeignClient(name = "events-management")
public interface UserEventClient extends UserEventInterface {
}
