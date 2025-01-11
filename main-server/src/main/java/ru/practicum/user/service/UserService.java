package ru.practicum.user.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto newUserDto);

    UserDto getUserById(Long userId);

    List<UserDto> getUsersByIdList(List<Long> ids, Pageable page);

    void deleteUser(Long userId);
}
