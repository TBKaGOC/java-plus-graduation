package ru.practicum.application.user.ui;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.application.api.dto.user.UserDto;
import ru.practicum.application.api.exception.NotFoundException;

@RequestMapping("/inner/user")
public interface InnerUserInterface {
    @GetMapping("/{userId}")
    UserDto getById(@PathVariable Long userId) throws NotFoundException;

    @GetMapping("/{userId}/exist")
    boolean existsById(@PathVariable Long userId);
}
