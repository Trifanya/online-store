package ru.devtrifanya.online_store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.models.CategoryRelation;

import java.util.List;

@Repository
public interface CategoryRelationRepository extends JpaRepository<CategoryRelation, Integer> {
    List<Category> findByParentId(int parentId);
}
