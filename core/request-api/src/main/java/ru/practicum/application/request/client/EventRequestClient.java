package ru.practicum.application.request.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.application.request.api.AbstractRequestInterface;

@FeignClient(name = "request-service")
public interface EventRequestClient extends AbstractRequestInterface {
}
