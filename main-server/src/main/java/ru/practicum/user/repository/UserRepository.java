package ru.practicum.user.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByName(String name);

    User getUserById(Long id);

    Boolean existsByName(String name);

    @Query("SELECT u FROM User u " +
            "WHERE(u.id in :ids)")
    List<User> findAllByIdsPageable(List<Long> ids, Pageable page);

    @Query("SELECT u.id FROM User u")
    List<Long> findAllId();
}
