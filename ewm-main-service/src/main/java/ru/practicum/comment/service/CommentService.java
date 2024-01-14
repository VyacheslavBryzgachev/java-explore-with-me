package ru.practicum.comment.service;

import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentDtoResponse;

import java.util.List;

public interface CommentService {

    void create(Long userId, Long eventId, CommentDto commentDto);

    CommentDtoResponse update(Long userId, Long eventId, CommentDto commentDto);

    List<CommentDtoResponse> getAll(Long eventId);

    CommentDtoResponse getById(Long eventId, Long commentId);

    void delete(Long commentId, Long userId);

}
