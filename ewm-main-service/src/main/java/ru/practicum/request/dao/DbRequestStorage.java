package ru.practicum.request.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.request.model.Request;
import ru.practicum.request.repository.RequestRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DbRequestStorage {

    private final RequestRepository repository;

    public List<Request> getRequests(Long userId) {
        return repository.getRequestsByRequester(userId);
    }

    public Request createRequest(Request request) {
        return repository.save(request);
    }

    public Request cancelRequest(Request request) {
        return repository.save(request);
    }

    public List<Request> getRequest(Long eventId) {
        return repository.getRequestsByEventId(eventId);
    }

    public Optional<Request> getRequestById(Long userId, Long requestId) {
        return repository.getRequestsByRequesterAndId(userId, requestId);
    }

    public List<Request> getRequestsByIds(List<Long> requestIds) {
        return repository.getRequestsByIdIs(requestIds);
    }

    public Request findRequest(Long userId, Long eventId) {
        return repository.findRequest(userId, eventId);
    }

    public Integer getConfirmedRequestsByEventId(Long eventId) {
        return repository.getConfirmedRequestsByEventId(eventId);
    }

    public List<Request> getConfirmedRequestsByEventIds(List<Long> eventIds) {
        return repository.getConfirmedRequestsByEventIds(eventIds);
    }
}
