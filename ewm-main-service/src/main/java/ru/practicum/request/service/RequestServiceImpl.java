package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.enums.State;
import ru.practicum.enums.Status;
import ru.practicum.event.dao.DbEventStorage;
import ru.practicum.event.model.Event;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.WrongRequestException;
import ru.practicum.request.dao.DbRequestStorage;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.user.dao.DbUserStorage;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final DbRequestStorage dbRequestStorage;
    private final DbEventStorage dbEventStorage;
    private final DbUserStorage dbUserStorage;
    private final RequestMapper requestMapper = new RequestMapper();

    @Override
    public List<ParticipationRequestDto> getRequests(Long userId) {
        return dbRequestStorage.getRequests(userId).stream()
                .map(requestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        User user = dbUserStorage.getById(userId).stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Event event = dbEventStorage.getEvent(eventId).stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));
        Request request = Request.builder()
                .created(LocalDateTime.now())
                .requester(user)
                .event(event)
                .build();
        if (dbRequestStorage.findRequest(userId, eventId) != null) {
            throw new WrongRequestException("Данный пользователь уже подал заявку на участие в этом событии");
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new WrongRequestException("Инициатор события не может добавить запрос на участие в своём событии");
        }
        if (event.getState().equals(State.CANCELED) || event.getState().equals(State.PENDING)) {
            throw new WrongRequestException("Нельзя участвовать в неопубликованном событии");
        }
        if (event.getParticipantLimit() > 0 && event.getParticipantLimit() <= dbRequestStorage.getConfirmedRequestsByEventId(eventId)) {
            throw new WrongRequestException("Достигнут лимит запросов на участие");
        }
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(Status.CONFIRMED);
        } else {
            request.setStatus(Status.PENDING);
        }
        return requestMapper.toParticipationRequestDto(dbRequestStorage.createRequest(request));
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        dbUserStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Request request = dbRequestStorage.getRequestById(userId, requestId).stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Запрос не найден"));
        if (!request.getRequester().getId().equals(userId)) {
            throw new WrongRequestException("Запрос не принадлежит пользователю");
        }
        request.setStatus(Status.CANCELED);
        return requestMapper.toParticipationRequestDto(dbRequestStorage.cancelRequest(request));
    }
}
