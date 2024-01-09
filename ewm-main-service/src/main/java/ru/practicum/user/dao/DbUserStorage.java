package ru.practicum.user.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class DbUserStorage {
    private final UserRepository userRepository;
    private final UserMapper userMapper = new UserMapper();

    public User create(UserDto userDto) {
        return userRepository.save(userMapper.toUser(userDto));
    }

    public Page<User> getAll(List<Long> ids, Pageable pageable) {
        return userRepository.getUsersByIds(ids, pageable);
    }

    public Page<User> getAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    public User findByName(String name) {
        return userRepository.findByName(name);
    }
}
