package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dao.DbStatStorage;
import ru.practicum.dto.StatDtoHitResponse;
import ru.practicum.dto.StatDtoRequest;
import ru.practicum.dto.StatDtoStatResponse;
import ru.practicum.mapper.StatMapper;
import ru.practicum.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {

    private final DbStatStorage dbStatStorage;
    private final StatMapper statMapper = new StatMapper();

    @Override
    public StatDtoHitResponse create(StatDtoRequest statDtoRequest) {
        Stat stat = statMapper.toStat(statDtoRequest);
        return statMapper.toStatDtoHitResponse(dbStatStorage.createStat(stat));
    }

    @Override
    public List<StatDtoStatResponse> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        return dbStatStorage.getStats(start, end, uris, unique);
    }
}
