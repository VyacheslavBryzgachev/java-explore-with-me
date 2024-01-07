package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.request.model.Request;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("SELECT r FROM Request r where r.requester.id = :requesterId")
    List<Request> getRequestsByRequester(@Param("requesterId") Long requesterId);

    @Query(value = "SELECT r FROM Request r where r.event.id = :eventId")
    List<Request> getRequestsByEventId(@Param("eventId") Long eventId);

    @Query("SELECT r FROM Request r where r.requester.id = :userId and r.id = :requestId")
    Optional<Request> getRequestsByRequesterAndId(@Param("userId") Long userId, @Param("requestId") Long requestId);

    @Query(value = "SELECT r FROM Request r WHERE r.id in (:requestIds)")
    List<Request> getRequestsByIdIs(@Param("requestIds") List<Long> requestIds);

    @Query(value = "SELECT r FROM Request r where r.requester.id = :userId and r.event.id = :eventId")
    Request findRequest(@Param("userId") Long userId, @Param("eventId") Long eventId);

    @Query(value = "SELECT count(r) from Request r where r.event.id = :eventId and r.status = 'CONFIRMED'")
    Integer getConfirmedRequestsByEventId(@Param("eventId") Long eventId);

    @Query(value = "SELECT r from Request r where r.event.id in :eventIds and r.status = 'CONFIRMED'")
    List<Request> getConfirmedRequestsByEventIds(@Param("eventIds") List<Long> eventIds);
}
