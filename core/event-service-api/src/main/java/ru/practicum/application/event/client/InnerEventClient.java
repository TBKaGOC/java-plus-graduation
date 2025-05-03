package ru.practicum.application.event.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.application.event.ui.InnerEventInterface;

@FeignClient(name = "event-service")
public interface InnerEventClient extends InnerEventInterface {
}
