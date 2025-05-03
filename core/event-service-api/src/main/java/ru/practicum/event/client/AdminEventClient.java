package ru.practicum.event.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.event.ui.AdminEventInterface;

@FeignClient(name = "events-management")
public interface AdminEventClient extends AdminEventInterface {
}
