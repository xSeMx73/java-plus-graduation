package ru.practicum.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByInitiatorId(Long userId, Pageable pageable);

    Optional<Event> findByIdAndInitiatorId(Long id, long initiatorId);

    @Query("SELECT e FROM Event AS e " +
            "WHERE ((:users) IS NULL OR e.initiator.id IN :users) " +
            "AND ((:states) IS NULL OR e.state IN :states) " +
            "AND ((:categories) IS NULL OR e.category.id IN :categories) " +
            "AND ((cast(:rangeStart as timestamp) IS NULL OR e.eventDate >= :rangeStart) " +
            "AND ((cast(:rangeEnd as timestamp) IS NULL OR e.eventDate <= :rangeEnd)))")
    List<Event> findEventsByAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                                  LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query("SELECT e FROM Event AS e " +
            "WHERE ((:text IS NULL OR :text = '') " +
            "OR UPPER(e.annotation) LIKE UPPER(CONCAT('%', :text, '%')) " +
            "OR UPPER(e.description) LIKE UPPER(CONCAT('%', :text, '%'))) " +
            "AND ((:categories) IS NULL OR e.category.id IN :categories) " +
            "AND (:paid IS NULL OR e.paid = :paid OR :paid IS NOT NULL) " +
            "AND ((cast(:rangeStart as timestamp) IS NULL OR e.eventDate >= :rangeStart) " +
            "AND ((cast(:rangeEnd as timestamp) IS NULL OR e.eventDate <= :rangeEnd))) " +
            "AND ( e.state = :eventState) " +
            "AND ((:onlyAvailable) IS NULL OR (:onlyAvailable) = false OR (e.participantLimit = 0))")
    List<Event> findEventsPublic(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                 LocalDateTime rangeEnd, EventState eventState,
                                 Boolean onlyAvailable, Pageable pageable);

    List<Event> findByCategoryId(Long id);

    Event findByInitiatorId(Long userId);
}
