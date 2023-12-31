package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.StatDtoStatResponse;
import ru.practicum.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatRepository extends JpaRepository<Stat, Long> {
    @Query("SELECT new ru.practicum.dto.StatDtoStatResponse(stat.app, stat.uri, COUNT(stat.ip)) " +
            "FROM Stat AS stat " +
            "WHERE stat.timestamp BETWEEN ?1 AND ?2 and stat.uri in ?3 " +
            "GROUP BY stat.ip, stat.uri, stat.app " +
            "ORDER BY COUNT(stat.ip) DESC ")
    List<StatDtoStatResponse> getStatByUniqueFalseWithUri(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "SELECT new ru.practicum.dto.StatDtoStatResponse(stat.app, stat.uri, COUNT(distinct stat.ip)) " +
            "FROM Stat AS stat " +
            "WHERE stat.timestamp BETWEEN ?1 AND ?2 and stat.uri in ?3 " +
            "GROUP BY stat.ip, stat.uri, stat.app " +
            "ORDER BY COUNT(stat.ip) DESC ")
    List<StatDtoStatResponse> getStatByUniqueTrueWithUri(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.dto.StatDtoStatResponse(stat.app, stat.uri, COUNT(stat.ip)) " +
            "FROM Stat AS stat " +
            "WHERE stat.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY stat.ip, stat.uri, stat.app " +
            "ORDER BY COUNT(stat.ip) DESC ")
    List<StatDtoStatResponse> getStatByUniqueFalse(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.dto.StatDtoStatResponse(stat.app, stat.uri, COUNT(distinct stat.ip)) " +
            "FROM Stat AS stat " +
            "WHERE stat.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY stat.ip, stat.uri, stat.app " +
            "ORDER BY COUNT(stat.ip) DESC ")
    List<StatDtoStatResponse> getStatByUniqueTrue(LocalDateTime start, LocalDateTime end);
}
