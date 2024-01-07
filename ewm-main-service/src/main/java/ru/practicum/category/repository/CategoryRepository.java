package ru.practicum.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.category.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c from Category c where lower(c.name) like lower(:name) and c.id not in (:catId)")
    Category findByName(@Param("name") String name, @Param("catId") Long catId);

    @Query("SELECT c from Category c where lower(c.name) like lower(:name)")
    Category findByName(@Param("name") String name);
}
