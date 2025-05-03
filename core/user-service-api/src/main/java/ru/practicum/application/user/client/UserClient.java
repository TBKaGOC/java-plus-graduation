package ru.practicum.application.user.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.application.user.api.AbstractUserInterface;

@FeignClient(name = "user-service")
public interface UserClient extends AbstractUserInterface {
}
