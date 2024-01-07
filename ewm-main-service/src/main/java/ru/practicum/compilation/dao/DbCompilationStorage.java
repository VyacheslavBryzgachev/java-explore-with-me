package ru.practicum.compilation.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DbCompilationStorage {

    private final CompilationRepository compilationRepository;

    public Compilation create(Compilation compilation) {
        return compilationRepository.save(compilation);
    }

    public void delete(Long compId) {
        compilationRepository.deleteById(compId);
    }

    public Compilation update(Compilation compilation) {
        return compilationRepository.save(compilation);
    }

    public Optional<Compilation> getById(Long compId) {
        return compilationRepository.findById(compId);
    }

    public Page<Compilation> getAllBySort(Boolean pinned, Pageable pageable) {
        return compilationRepository.getCompilationsBySort(pinned, pageable);
    }

    public Page<Compilation> getAll(Pageable pageable) {
        return compilationRepository.findAll(pageable);
    }
}
