package ru.practicum.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.user.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT u FROM User u where u.id IN (:ids)")
    Page<User> getUsersByIds(@Param("ids") List<Long> ids, Pageable pageable);

    @Query("SELECT u FROM User u where lower(u.name) like lower(:name)")
    User findByName(@Param("name") String name);
}
