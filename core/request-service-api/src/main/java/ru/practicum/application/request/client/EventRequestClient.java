package ru.practicum.application.request.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.application.request.ui.EventRequestInterface;

@FeignClient(name = "request-service")
public interface EventRequestClient extends EventRequestInterface {
}
