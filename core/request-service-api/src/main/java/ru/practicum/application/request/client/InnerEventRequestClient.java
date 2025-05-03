package ru.practicum.application.request.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.application.request.ui.InnerEventRequestInterface;

@FeignClient(name = "request-service")
public interface InnerEventRequestClient extends InnerEventRequestInterface {
}
