package ru.practicum.request.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.request.ui.EventRequestInterface;

@FeignClient(name = "request-service")
public interface EventRequestClient extends EventRequestInterface {
}
