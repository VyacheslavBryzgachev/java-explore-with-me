package ru.practicum.mapper;

import ru.practicum.dto.StatDtoHitResponse;
import ru.practicum.dto.StatDtoRequest;
import ru.practicum.model.Stat;

public class StatMapper {

    public Stat toStat(StatDtoRequest statDtoRequest) {
        return Stat.builder()
                .app(statDtoRequest.getApp())
                .ip(statDtoRequest.getIp())
                .uri(statDtoRequest.getUri())
                .timestamp(statDtoRequest.getTimestamp())
                .build();
    }

    public StatDtoHitResponse toStatDtoHitResponse(Stat stat) {
        return StatDtoHitResponse.builder()
                .app(stat.getApp())
                .ip(stat.getIp())
                .uri(stat.getUri())
                .build();
    }
}
