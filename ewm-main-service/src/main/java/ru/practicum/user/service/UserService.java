package ru.practicum.user.service;

import ru.practicum.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto create(UserDto userDto);

    void delete(Long id);

    List<UserDto> getAll(Integer from, Integer size, List<Long> ids);
}
