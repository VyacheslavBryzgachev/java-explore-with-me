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
    @Query(value = "SELECT app, uri, count(ip) as hits FROM statistic " +
            "WHERE create_time BETWEEN :start AND :end AND uri in(:uris)" +
            " group by app, uri order by count(ip) desc",
            nativeQuery = true)
    List<StatDtoStatResponse> getStatByUniqueFalseWithUri(@Param("start") LocalDateTime start,
                                                          @Param("end") LocalDateTime end, List<String> uris);

    @Query(value = "SELECT app, uri, count(DISTINCT ip) as hits FROM statistic " +
            "WHERE create_time BETWEEN :start AND :end AND uri in(:uris)" +
            "group by app, uri order by count(ip) desc",
            nativeQuery = true)
    List<StatDtoStatResponse> getStatByUniqueTrueWithUri(@Param("start") LocalDateTime start,
                                                         @Param("end") LocalDateTime end, List<String> uris);

    @Query(value = "SELECT app, uri, count(ip) as hits FROM statistic " +
            "WHERE create_time BETWEEN :start AND :end" +
            " group by app, uri order by count(ip) desc",
            nativeQuery = true)
    List<StatDtoStatResponse> getStatByUniqueFalse(@Param("start") LocalDateTime start,
                                                          @Param("end") LocalDateTime end);

    @Query(value = "SELECT app, uri, count(DISTINCT ip) as hits FROM statistic " +
            "WHERE create_time BETWEEN :start AND :end " +
            "group by app, uri order by count(ip) desc",
            nativeQuery = true)
    List<StatDtoStatResponse> getStatByUniqueTrue(@Param("start") LocalDateTime start,
                                                         @Param("end") LocalDateTime end);
}
