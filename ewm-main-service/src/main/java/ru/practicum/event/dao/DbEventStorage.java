package ru.practicum.event.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventsRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DbEventStorage {

    private final EventsRepository repository;

    public List<Event> getUserEvents(Long id, Pageable pageable) {
        return repository.getEventsByInitiator(id, pageable);
    }

    public Event createEvent(Event event) {
        return repository.save(event);
    }

    public Optional<Event> getEvent(Long id, Long eventId) {
        return repository.getEventByInitiatorAndId(id, eventId);
    }

    public Optional<Event> getEvent(Long id) {
        return repository.getEventById(id);
    }

    public List<Event> getEventsByIds(Set<Long> ids) {
        return repository.getEventsByIds(ids);
    }

    public Optional<Event> getEventById(Long eventId) {
        return repository.findById(eventId);
    }

    public Page<Event> publicGetAllEvents(Specification<Event> specification, Pageable pageable) {
        return repository.findAll(specification, pageable);
    }

    public Page<Event> adminGetAllEvents(Specification<Event> specification, Pageable pageable) {
        return repository.findAll(specification, pageable);
    }

    public Event getEventByCatId(Long catId) {
        return repository.getEventByCatId(catId);
    }
}
