package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.models.CategoryRelation;
import ru.devtrifanya.online_store.repositories.CategoryRelationRepository;

@Service
@Transactional(readOnly = true)
@Data
public class CategoryRelationService {
    private final CategoryRelationRepository categoryRelationRepository;

    /**
     * В метод передается отношение, которому присвоено только поле child, поэтому
     * данный метод инициализирует у сохраняемого отношения поле parent, а затем вызывает
     * метод репозитория, который сохраняет это отношение в БД.
     */
    public CategoryRelation createCategoryRelation(CategoryRelation relationToSave,
                                                   Category parent) {
        relationToSave.setParent(parent);
        return categoryRelationRepository.save(relationToSave);
    }

    /**
     * В метод передается отношение, которому присвоено только поле child, поэтому
     * данный метод инициализирует id, чтобы указать, отношение с каким id будет изменено,
     * затем инициализирует у сохраняемого отношения поле parent, а затем вызывает метод
     * репозитория, который сохраняет это отношение в БД.
     */
    public CategoryRelation updateCategoryRelation(int relationId,
                                                   CategoryRelation updatedRelation,
                                                   Category parent) {
        updatedRelation.setId(relationId);
        updatedRelation.setParent(parent);
        return categoryRelationRepository.save(updatedRelation);
    }
}
