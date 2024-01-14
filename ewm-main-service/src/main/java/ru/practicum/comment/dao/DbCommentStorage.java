package ru.practicum.comment.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.repository.CommentRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DbCommentStorage {

    private final CommentRepository commentRepository;

    public void create(Comment comment) {
        commentRepository.save(comment);
    }

    public Optional<Comment> getById(Long commentId) {
        return commentRepository.findById(commentId);
    }

    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    public Optional<Comment> getById(Long eventId, Long commentId) {
        return commentRepository.findByIdAndEventId(commentId, eventId);
    }

    public void delete(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
