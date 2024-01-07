package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.exceptions.WrongRequestException;
import ru.practicum.user.dao.DbUserStorage;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final DbUserStorage dbUserStorage;
    private final UserMapper userMapper = new UserMapper();

    @Override
    public UserDto create(UserDto userDto) {
        if (dbUserStorage.findByName(userDto.getName()) != null) {
            throw new WrongRequestException("Пользователь с таким именем уже существует");
        }
        return userMapper.toUserDto(dbUserStorage.create(userDto));
    }

    @Override
    public void delete(Long id) {
        dbUserStorage.delete(id);
    }

    @Override
    public List<UserDto> getAll(Integer from, Integer size, List<Long> ids) {
        Pageable pageable = PageRequest.of(from, size);
        if (ids == null) {
            return dbUserStorage.getAll(pageable).stream()
                    .map(userMapper::toUserDto)
                    .collect(Collectors.toList());
        }
        return dbUserStorage.getAll(ids, pageable).stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
