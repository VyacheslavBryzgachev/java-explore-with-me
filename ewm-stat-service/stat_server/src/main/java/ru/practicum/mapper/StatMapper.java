package ru.practicum.mapper;

import ru.practicum.dto.StatDtoHitResponse;
import ru.practicum.dto.StatDtoRequest;
import ru.practicum.model.Stat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StatMapper {

    public Stat toStat(StatDtoRequest statDtoRequest) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return Stat.builder()
                .app(statDtoRequest.getApp())
                .ip(statDtoRequest.getIp())
                .uri(statDtoRequest.getUri())
                .timestamp(LocalDateTime.parse(statDtoRequest.getTimestamp(), formatter))
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
