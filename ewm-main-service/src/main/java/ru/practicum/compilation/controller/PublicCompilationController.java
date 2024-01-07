package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Validated
public class PublicCompilationController {

    private final CompilationService compilationService;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<CompilationDto> getAll(@RequestParam(name = "pinned", required = false) Boolean pinned,
                                       @RequestParam(value = "from", defaultValue = "0", required = false) @PositiveOrZero Integer from,
                                       @RequestParam(value = "size", defaultValue = "10", required = false) @Positive Integer size) {
        return compilationService.getCompBySort(pinned, from, size);
    }

    @GetMapping("/{compId}")
    @ResponseStatus(value = HttpStatus.OK)
    public CompilationDto getById(@PathVariable Long compId) {
        return compilationService.getById(compId);
    }
}
