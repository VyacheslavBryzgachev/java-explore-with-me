package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.StatDtoRequest;
import ru.practicum.service.StatService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/hit")
@Validated
public class HitController {
    private final StatService statService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createHit(@Valid @RequestBody StatDtoRequest statsDto) {
        statService.create(statsDto);
    }
}