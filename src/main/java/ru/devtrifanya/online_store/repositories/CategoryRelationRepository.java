package ru.devtrifanya.online_store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.models.CategoryRelation;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRelationRepository extends JpaRepository<CategoryRelation, Integer> {
    List<CategoryRelation> parentIdIsNull();

    List<CategoryRelation> findAllByParentId(int parentId);

    List<CategoryRelation> findAllByChildId(int childId);

    boolean existsByParentId(int parentId);

    boolean existsByChildId(int childId);

    boolean existsByParentIdAndChildId(int parentId, int childId);
}
