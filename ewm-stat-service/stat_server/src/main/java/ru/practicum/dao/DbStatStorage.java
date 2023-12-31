package ru.practicum.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.dto.StatDtoStatResponse;
import ru.practicum.model.Stat;
import ru.practicum.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DbStatStorage {

    private final StatRepository statRepository;

    public Stat createStat(Stat stat) {
        return statRepository.save(stat);
    }

    public List<StatDtoStatResponse> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (uris == null) {
            if (unique) {
                return statRepository.getStatByUniqueTrue(start, end);
            } else {
                return statRepository.getStatByUniqueFalse(start, end);
            }
        } else {
            if (unique) {
                return statRepository.getStatByUniqueTrueWithUri(start, end, uris);
            } else {
                return statRepository.getStatByUniqueFalseWithUri(start, end, uris);
            }
        }
    }
}
