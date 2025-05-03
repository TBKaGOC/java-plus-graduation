package ru.practicum.user.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.api.dto.user.UserDto;
import ru.practicum.api.exception.ConflictException;
import ru.practicum.user.service.UserService;
import ru.practicum.user.ui.UserInterface;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserController implements UserInterface {

    final UserService userService;

    @Override
    public List<UserDto> getUsersList(List<Long> ids,
                                      Integer from,
                                      Integer size) {
        return userService.getUsersByIdList(ids, PageRequest.of(from, size));
    }

    @Override
    public UserDto addUser(UserDto newUser) throws ConflictException {
        return userService.addUser(newUser);
    }

    @Override
    public void deleteUser(Long userId) {
        userService.deleteUser(userId);
    }

}
