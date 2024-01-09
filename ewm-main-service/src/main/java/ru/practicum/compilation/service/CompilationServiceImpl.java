package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dao.DbCompilationStorage;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.dao.DbEventStorage;
import ru.practicum.event.model.Event;
import ru.practicum.exceptions.NotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final DbCompilationStorage dbCompilationStorage;
    private final DbEventStorage eventStorage;
    private final CompilationMapper compilationMapper = new CompilationMapper();

    @Override
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        Set<Event> events = new HashSet<>(eventStorage.getEventsByIds(newCompilationDto.getEvents()));
        Compilation compilation = Compilation.builder()
                .title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.getPinned() != null ? newCompilationDto.getPinned() : false)
                .events(events)
                .build();
        return compilationMapper.toCompilationDto(dbCompilationStorage.create(compilation));
    }

    @Override
    public void delete(Long compId) {
        dbCompilationStorage.delete(compId);
    }

    @Override
    public CompilationDto update(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = dbCompilationStorage.getById(compId).stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Подборка не найдена"));
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        if (updateCompilationRequest.getEvents() != null) {
            Set<Event> events = new HashSet<>(eventStorage.getEventsByIds(updateCompilationRequest.getEvents()));
            compilation.setEvents(events);
        }
        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        return compilationMapper.toCompilationDto(dbCompilationStorage.update(compilation));
    }

    @Override
    public List<CompilationDto> getCompBySort(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        if (pinned != null) {
            return dbCompilationStorage.getAllBySort(pinned, pageable).stream()
                    .map(compilationMapper::toCompilationDto)
                    .collect(Collectors.toList());
        }
        return dbCompilationStorage.getAll(pageable).stream()
                .map(compilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getById(Long compId) {
        Compilation compilation = dbCompilationStorage.getById(compId).stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Подборка не найдена"));
        return compilationMapper.toCompilationDto(compilation);
    }
}
