package ru.practicum.user.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.api.dto.user.UserDto;
import ru.practicum.api.exception.NotFoundException;
import ru.practicum.user.service.UserService;
import ru.practicum.user.ui.InnerUserInterface;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InnerUserController implements InnerUserInterface {

    final UserService userService;

    @Override
    public UserDto getById(Long userId) throws NotFoundException {
        return userService.getUserById(userId);
    }

    @Override
    public boolean existsById(Long userId) {
        return userService.existById(userId);
    }
}
