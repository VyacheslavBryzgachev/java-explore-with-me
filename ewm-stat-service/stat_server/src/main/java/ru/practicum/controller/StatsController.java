package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<List<StatDtoStatResponse>> getStats(
            @RequestParam(value = "start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam(value = "end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(value = "uris", required = false) List<String> uris,
            @RequestParam(value = "unique", defaultValue = "false", required = false) Boolean unique) {
        if (start.isAfter(end)) {
            return ResponseEntity.badRequest().build();
        }
        List<StatDtoStatResponse> result = statService.getStats(start, end, uris, unique);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/view/{eventId}")
    public Long getView(@PathVariable Long eventId) {
        return statService.getViewStats(eventId);
    }
}
