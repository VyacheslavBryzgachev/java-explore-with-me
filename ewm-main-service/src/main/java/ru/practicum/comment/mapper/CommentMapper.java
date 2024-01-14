package ru.practicum.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentDtoResponse;
import ru.practicum.comment.model.Comment;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import java.util.function.Function;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CommentMapper extends Function<Comment, CommentDto> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "commentDto.text")
    @Mapping(target = "commentator", source = "user")
    @Mapping(target = "event", source = "event")
    @Mapping(target = "createdTime", source = "commentDto.createdTime")
    Comment toEntity(CommentDto commentDto, User user, Event event);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "commentDto.text")
    @Mapping(target = "commentator", ignore = true)
    @Mapping(target = "event", ignore = true)
    Comment toEntity(CommentDto commentDto);

    CommentDto toDto(Comment comment);

    CommentDtoResponse toCommentDtoResponse(Comment comment);

}