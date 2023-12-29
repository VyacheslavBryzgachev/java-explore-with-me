package ru.practicum.service;

import ru.practicum.dto.StatDtoRequest;
import ru.practicum.dto.StatDtoStatResponse;
import ru.practicum.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {

    Stat create(StatDtoRequest statDtoRequest);

    List<StatDtoStatResponse> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
