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
}
