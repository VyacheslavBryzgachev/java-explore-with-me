package ru.practicum.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.service.RequestService;

import java.util.List;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @GetMapping("/{userId}/requests")
    @ResponseStatus(value = HttpStatus.OK)
    public List<ParticipationRequestDto> getRequests(@PathVariable Long userId) {
        return requestService.getRequests(userId);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        return requestService.createRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    @ResponseStatus(value = HttpStatus.OK)
    public ParticipationRequestDto cancelRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        return requestService.cancelRequest(userId, requestId);
    }
}
