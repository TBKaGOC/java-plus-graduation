package ru.practicum.application.event.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.application.event.ui.AdminEventInterface;

@FeignClient(name = "events-management")
public interface AdminEventClient extends AdminEventInterface {
}
