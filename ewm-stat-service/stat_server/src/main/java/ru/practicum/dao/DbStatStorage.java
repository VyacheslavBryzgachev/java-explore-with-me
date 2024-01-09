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

    public void createStat(Stat stat) {
        statRepository.save(stat);
    }

    public List<StatDtoStatResponse> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
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

    public Long countDistinctByUri(String s) {
        return statRepository.countDistinctByUri(s);
    }
}
