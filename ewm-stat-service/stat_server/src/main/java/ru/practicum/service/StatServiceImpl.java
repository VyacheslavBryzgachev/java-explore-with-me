package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dao.DbStatStorage;
import ru.practicum.dto.StatDtoRequest;
import ru.practicum.dto.StatDtoStatResponse;
import ru.practicum.mapper.StatMapper;
import ru.practicum.model.Stat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {

    private final DbStatStorage dbStatStorage;
    private final StatMapper statMapper = new StatMapper();

    @Override
    public void create(StatDtoRequest statDtoRequest) {
        Stat stat = statMapper.toStat(statDtoRequest);
        dbStatStorage.createStat(stat);
    }

    @Override
    public List<StatDtoStatResponse> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        return dbStatStorage.getStats(start, end, uris, unique);
    }

    @Override
    public Long getViewStats(Long eventId) {
        Long view = dbStatStorage.countDistinctByUri("/events/" + eventId);
        return Objects.requireNonNullElse(view, 0L);
    }
}
