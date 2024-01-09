package ru.practicum.mapper;

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
}
