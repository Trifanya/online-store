package ru.devtrifanya.online_store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.devtrifanya.online_store.models.Category;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByName(String name);

    @Query(value = "select category.* from category join category_relation on " +
            "category.id = category_relation.child_id where parent_id is null",
            nativeQuery = true)
    List<Category> findTopCategories();
}
