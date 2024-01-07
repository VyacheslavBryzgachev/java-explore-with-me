package ru.practicum.compilation.mapper;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.mapper.EventMapper;

import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper {

    private final EventMapper eventMapper = new EventMapper();

    public CompilationDto toCompilationDto(Compilation compilation) {
        List<EventShortDto> events = compilation.getEvents().stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .events(events)
                .pinned(compilation.getPinned())
                .build();
    }
}
