package ru.practicum.event.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.event.model.Location;
import ru.practicum.event.repository.LocationsRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DbLocationStorage {
    private final LocationsRepository locationsRepository;

    public Location create(Location location) {
        return locationsRepository.save(location);
    }

    public Optional<Location> getById(Long id) {
        return locationsRepository.findById(id);
    }
}
