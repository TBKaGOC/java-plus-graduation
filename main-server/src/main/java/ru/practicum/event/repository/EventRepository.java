package ru.practicum.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByIdIn(List<Long> ids);

    List<Event> findAllByCategoryId(Long catId);

    Page<Event> findAll(Pageable page);

    List<Event> findAllByInitiator(User user, Pageable page);

    @Query(value = "INSERT INTO locations () valuse (:lat, :lon)", nativeQuery = true)
    void saveLocation(Float lat, Float lon);

    @Query("SELECT e FROM Event e " +
           "WHERE e.initiator IN :users " +
           "AND e.state IN :states " +
           "AND e.category IN :categories " +
           "AND e.eventDate < :rangeStart " +
           "LIMIT :size")
    List<Event> findByParametersWithoutEnd(List<Long> users, List<String> states, List<Long> categories, String rangeStart, Integer size);

    @Query("SELECT e FROM Event e " +
            "WHERE e.initiator IN :users " +
            "AND e.state IN :states " +
            "AND e.category IN :categories " +
            "AND e.eventDate < :rangeStart " +
            "AND e.eventDate > :rangeEnd " +
            "LIMIT :size")
    List<Event> findByParametersWithEnd(List<Long> users, List<String> states, List<Long> categories, String rangeStart, String rangeEnd, Integer size);
}
