package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.dto.StatDtoStatResponse;
import ru.practicum.service.StatService;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
@RequestMapping(path = "/stats")
public class StatsController {

    private final StatService statService;

    @GetMapping
    public List<StatDtoStatResponse> getStats(
            @RequestParam(value = "start") LocalDateTime start,
            @RequestParam(value = "end") LocalDateTime end,
            @RequestParam(value = "uris", required = false) List<String> uris,
            @RequestParam(value = "unique", defaultValue = "false", required = false) boolean unique) {

        return statService.getStats(start, end, uris, unique);
    }
}
