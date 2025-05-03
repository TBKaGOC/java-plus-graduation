package ru.practicum.user.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.user.ui.InnerUserInterface;

@FeignClient(name = "user-service")
public interface InnerUserClient extends InnerUserInterface {
}
