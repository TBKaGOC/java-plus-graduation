package ru.practicum.event.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.event.ui.InnerEventInterface;

@FeignClient(name = "event-service")
public interface InnerEventClient extends InnerEventInterface {
}
