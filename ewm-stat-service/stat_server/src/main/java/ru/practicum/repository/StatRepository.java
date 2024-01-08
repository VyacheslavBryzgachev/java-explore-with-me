package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.StatDtoStatResponse;
import ru.practicum.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatRepository extends JpaRepository<Stat, Long> {
@Query("SELECT new ru.practicum.dto.StatDtoStatResponse(stat.app, stat.uri, COUNT(stat.ip)) " +
        "FROM Stat AS stat " +
        "WHERE stat.timestamp BETWEEN :start AND :end and stat.uri in (:uris) " +
        "GROUP BY stat.ip, stat.uri, stat.app " +
        "ORDER BY COUNT(stat.ip) DESC ")
    List<StatDtoStatResponse> getStatByUniqueFalseWithUri(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uris") List<String> uris);

    @Query(value = "SELECT new ru.practicum.dto.StatDtoStatResponse(stat.app, stat.uri, COUNT(distinct stat.ip)) " +
            "FROM Stat AS stat " +
            "WHERE stat.timestamp BETWEEN :start AND :end and stat.uri in (:uris) " +
            "GROUP BY stat.ip, stat.uri, stat.app " +
            "ORDER BY COUNT(stat.ip) DESC ")
    List<StatDtoStatResponse> getStatByUniqueTrueWithUri(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.dto.StatDtoStatResponse(stat.app, stat.uri, COUNT(stat.ip)) " +
            "FROM Stat AS stat " +
            "WHERE stat.timestamp BETWEEN :start AND :end " +
            "GROUP BY stat.ip, stat.uri, stat.app " +
            "ORDER BY COUNT(stat.ip) DESC ")
    List<StatDtoStatResponse> getStatByUniqueFalse(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT new ru.practicum.dto.StatDtoStatResponse(stat.app, stat.uri, COUNT(distinct stat.ip)) " +
            "FROM Stat AS stat " +
            "WHERE stat.timestamp BETWEEN :start AND :end " +
            "GROUP BY stat.ip, stat.uri, stat.app " +
            "ORDER BY COUNT(stat.ip) DESC ")
    List<StatDtoStatResponse> getStatByUniqueTrue(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
