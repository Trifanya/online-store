package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.models.CategoryRelation;
import ru.devtrifanya.online_store.repositories.CategoryRelationRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@Data
public class CategoryRelationService {
    private final CategoryRelationRepository relationRepository;

    /**
     * Получение отношений между категориями по указанному childId.
     * Метод получает на вход id дочерней категории, затем вызывает метод репозитория для
     * получения всех отношений дочерней категории с родительскими категориями и возвращает
     * полученный список.
     */
    public List<CategoryRelation> getRelationsByChildId(int childId) {
        return relationRepository.findAllByChildId(childId);
    }

    /**
     * Получение отношений между категориями по указанному parentId.
     * Метод получает на вход id родительской категории, затем вызывает метод репозитория для
     * получения всех отношений родительской категории с дочерними категориями и возвращает
     * полученный список.
     */
    public List<CategoryRelation> getRelationsByParentId(int parentId) {
        return relationRepository.findAllByParentId(parentId);
    }

    /**
     * В метод передается отношение, которому присвоено только поле child, поэтому
     * данный метод инициализирует у сохраняемого отношения поле parent, а затем вызывает
     * метод репозитория, который сохраняет это отношение в БД.
     */
    public CategoryRelation createCategoryRelation(CategoryRelation relationToSave,
                                                   Category parent) {
        relationToSave.setParent(parent);
        return relationRepository.save(relationToSave);
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
        return relationRepository.save(updatedRelation);
    }

    public void updateRelationsOfDeletingCategory(int categoryId) {
        List<CategoryRelation> relationsWithParents = relationRepository.findAllByChildId(categoryId);
        List<CategoryRelation> relationsWithChildren = relationRepository.findAllByParentId(categoryId);

        for (int i = 0; i < relationsWithParents.size(); i++) {
            for (int j = 0; j < relationsWithChildren.size(); j++) {
                CategoryRelation relation = new CategoryRelation();
                relation.setChild(relationsWithChildren.get(j).getChild());

                this.createCategoryRelation(
                        relation,
                        relationsWithParents.get(i).getParent()
                );
            }
        }
    }
}
