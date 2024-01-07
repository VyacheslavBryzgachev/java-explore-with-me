package ru.practicum.event.mapper;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.Event;
import ru.practicum.user.dto.UserDto;

public class EventMapper {
    public EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .eventDate(event.getEventDate())
                .annotation(event.getAnnotation())
                .title(event.getTitle())
                .views(event.getViews())
                .initiator(UserDto.builder()
                        .id(event.getInitiator().getId())
                        .name(event.getInitiator().getName())
                        .email(event.getInitiator().getEmail())
                        .build())
                .paid(event.getPaid())
                .category(CategoryDto.builder()
                        .id(event.getCategory().getId())
                        .name(event.getCategory().getName())
                        .build())
                .build();
    }

    public Event toEvent(NewEventDto newEventDto) {
        return Event.builder()
                .title(newEventDto.getTitle())
                .requestModeration(newEventDto.getRequestModeration() != null ? newEventDto.getRequestModeration() : true)
                .participantLimit(newEventDto.getParticipantLimit() != null ? newEventDto.getParticipantLimit() : 0)
                .description(newEventDto.getDescription())
                .paid(newEventDto.getPaid() != null ? newEventDto.getPaid() : false)
                .annotation(newEventDto.getAnnotation())
                .eventDate(newEventDto.getEventDate())
                .build();
    }

    public EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .state(event.getState())
                .createdOn(event.getCreatedOn())
                .publishedOn(event.getPublishedOn())
                .annotation(event.getAnnotation())
                .eventDate(event.getEventDate())
                .location(event.getLocation())
                .participantLimit(event.getParticipantLimit())
                .title(event.getTitle())
                .views(event.getViews())
                .requestModeration(event.getRequestModeration())
                .initiator(UserDto.builder()
                        .id(event.getInitiator().getId())
                        .email(event.getInitiator().getEmail())
                        .name(event.getInitiator().getName())
                        .build())
                .paid(event.getPaid())
                .description(event.getDescription())
                .category(CategoryDto.builder()
                        .id(event.getCategory().getId())
                        .name(event.getCategory().getName())
                        .build())
                .build();
    }
}
