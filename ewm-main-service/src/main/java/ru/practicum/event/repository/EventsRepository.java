package ru.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.event.model.Event;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EventsRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    @Query(value = "SELECT * FROM Events where initiator = :initiator", nativeQuery = true)
    List<Event> getEventsByInitiator(Long initiator, Pageable pageable);

    @Query(value = "SELECT * FROM Events where initiator = :initiator and id = :id", nativeQuery = true)
    Optional<Event> getEventByInitiatorAndId(Long initiator, Long id);

    Optional<Event> getEventById(Long id);

    @Query(value = "SELECT e FROM Event e  where e.id in(:ids)")
    List<Event> getEventsByIds(Set<Long> ids);

    @Query("SELECT e FROM Event e where e.category.id = :catId")
    Event getEventByCatId(@Param("catId") Long catId);
}
