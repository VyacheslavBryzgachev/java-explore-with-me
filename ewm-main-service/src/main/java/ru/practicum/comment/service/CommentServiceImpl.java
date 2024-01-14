package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.comment.dao.DbCommentStorage;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentDtoResponse;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.event.dao.DbEventStorage;
import ru.practicum.event.model.Event;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.WrongRequestException;
import ru.practicum.user.dao.DbUserStorage;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final DbUserStorage dbUserStorage;
    private final DbEventStorage dbEventStorage;
    private final DbCommentStorage dbCommentStorage;
    private final CommentMapper commentMapper;

    @Override
    public void create(Long userId, Long eventId, CommentDto commentDto) {
        final User user = dbUserStorage.getById(userId).stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        final Event event = dbEventStorage.getEventById(eventId).stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));
        commentDto.setCreatedTime(LocalDateTime.now());
        dbCommentStorage.create(commentMapper.toEntity(commentDto, user, event));
    }

    @Override
    public CommentDtoResponse update(Long userId, Long commentId, CommentDto commentDto) {
        final User user = dbUserStorage.getById(userId).stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        final Comment comment = dbCommentStorage.getById(commentId).stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Комментарий не найден"));
        if (!comment.getCommentator().equals(user)) {
            throw new WrongRequestException("Комментарий не принадлежит пользователю");
        }
        comment.setText(commentDto.getText());
        dbCommentStorage.create(comment);

        return commentMapper.toCommentDtoResponse(comment);
    }

    @Override
    public List<CommentDtoResponse> getAll(Long eventId) {
        return dbCommentStorage.getAll().stream()
                .filter(comment -> comment.getEvent().getId().equals(eventId))
                .map(commentMapper::toCommentDtoResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDtoResponse getById(Long eventId, Long commentId) {
        return dbCommentStorage.getById(eventId, commentId).stream()
                .findAny()
                .map(commentMapper::toCommentDtoResponse)
                .orElseThrow(() -> new NotFoundException("Комментарий не найден"));
    }

    @Override
    public void delete(Long commentId, Long userId) {
        final User user = dbUserStorage.getById(userId).stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        final Comment comment = dbCommentStorage.getById(commentId).stream()
                .findAny()
                .orElseThrow(() -> new NotFoundException("Комментарий не найден"));
        if (!comment.getCommentator().equals(user)) {
            throw new WrongRequestException("Комментарий не принадлежит пользователю");
        }
        dbCommentStorage.delete(commentId);
    }
}
