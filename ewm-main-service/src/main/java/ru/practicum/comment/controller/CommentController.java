package ru.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentDtoResponse;
import ru.practicum.comment.service.CommentService;

import javax.validation.Valid;
import java.util.List;

@RestControllerAdvice
@RequestMapping("/comment")
@RequiredArgsConstructor
@Validated
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestHeader Long userId, @RequestHeader Long eventId, @Valid @RequestBody CommentDto commentDto) {
        commentService.create(userId, eventId, commentDto);
    }

    @PatchMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDtoResponse update(@RequestHeader Long userId, @PathVariable Long commentId, @RequestBody CommentDto commentDto) {
        return commentService.update(userId, commentId, commentDto);
    }

    @GetMapping("/event/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDtoResponse> getAll(@PathVariable Long eventId) {
        return commentService.getAll(eventId);
    }

    @GetMapping("/event/{eventId}/comment/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDtoResponse getById(@PathVariable Long commentId, @PathVariable Long eventId) {
        return commentService.getById(eventId, commentId);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long commentId, @RequestHeader Long userId) {
        commentService.delete(commentId, userId);
    }
}
