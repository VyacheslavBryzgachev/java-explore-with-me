package ru.practicum.service;

import ru.practicum.dto.StatDtoRequest;
import ru.practicum.dto.StatDtoStatResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {

    void create(StatDtoRequest statDtoRequest);

    List<StatDtoStatResponse> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

    Long getViewStats(Long eventId);
}
