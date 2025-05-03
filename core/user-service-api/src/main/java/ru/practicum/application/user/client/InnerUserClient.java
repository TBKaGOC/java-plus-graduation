package ru.practicum.application.user.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.application.user.ui.InnerUserInterface;

@FeignClient(name = "user-service")
public interface InnerUserClient extends InnerUserInterface {
}
