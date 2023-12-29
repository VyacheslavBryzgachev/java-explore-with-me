package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.StatDtoRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/hit")
public class HitController {

    private final StatsClient statsClient;

    @PostMapping
    public ResponseEntity<Object> createHit(@RequestBody StatDtoRequest statsDto) {
        return statsClient.createStat(statsDto);
    }
}
