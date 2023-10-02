package ru.devtrifanya.online_store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.models.CategoryRelation;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRelationRepository extends JpaRepository<CategoryRelation, Integer> {
    List<CategoryRelation> findAllByParentId(int parentId);

    Optional<CategoryRelation> findByChildId(int childId);

    Optional<CategoryRelation> findByChildIdAndParentId(int childId, int parentId);
}
