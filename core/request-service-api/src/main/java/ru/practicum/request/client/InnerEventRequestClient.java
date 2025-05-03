package ru.practicum.request.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.request.ui.InnerEventRequestInterface;

@FeignClient(name = "request-service")
public interface InnerEventRequestClient extends InnerEventRequestInterface {
}
