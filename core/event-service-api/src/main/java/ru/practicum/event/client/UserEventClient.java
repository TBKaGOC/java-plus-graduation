package ru.practicum.event.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.event.ui.UserEventInterface;

@FeignClient(name = "events-management")
public interface UserEventClient extends UserEventInterface {
}
