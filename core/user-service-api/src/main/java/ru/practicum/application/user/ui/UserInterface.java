package ru.practicum.application.user.ui;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.application.api.dto.user.UserDto;
import ru.practicum.application.api.exception.ConflictException;

import java.util.List;

@RequestMapping("/admin/users")
public interface UserInterface {
    @GetMapping
    List<UserDto> getUsersList(@RequestParam(required = false) List<Long> ids,
                               @RequestParam(required = false, defaultValue = "0") Integer from,
                               @RequestParam(required = false, defaultValue = "10") Integer size);

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserDto addUser(@Valid @RequestBody UserDto newUser) throws ConflictException;

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(@PathVariable Long userId);
}
