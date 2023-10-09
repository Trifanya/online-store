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

    public List<CategoryRelation> getRelationsByChildId(int childId) {
        return relationRepository.findAllByChildId(childId);
    }

    public CategoryRelation createCategoryRelation(Category child, Category parent) {
        CategoryRelation relationToSave = new CategoryRelation();
        relationToSave.setChild(child);
        relationToSave.setParent(parent);

        return relationRepository.save(relationToSave);
    }

    public CategoryRelation updateCategoryRelation(int relationId,
                                                   CategoryRelation updatedRelation) {
        updatedRelation.setId(relationId);
        return relationRepository.save(updatedRelation);
    }

    public void updateRelationsOfDisplacedCategory(int categoryId, Category newParent) {
        List<CategoryRelation> relationsWithParents = relationRepository.findAllByChildId(categoryId);

        for (CategoryRelation relationWithParent : relationsWithParents) {
            relationWithParent.setParent(newParent);
        }
    }

    /**
     * Связывание родительской категории переданной категории с ее дочерними категориями.
     * Метод получает на вход id категории, которую планируется удалить, затем вызывает
     * методы репозитория для получения связей этой категории с родительскими категориями
     * и с дочерними категориями и наконец создает новые отношения между полученными
     * родительскими и дочерими категориями.
     */
    public void updateRelationsOfDeletingCategory(int categoryId) {
        List<CategoryRelation> relationsWithParents = relationRepository.findAllByChildId(categoryId);
        List<CategoryRelation> relationsWithChildren = relationRepository.findAllByParentId(categoryId);

        for (int i = 0; i < relationsWithParents.size(); i++) {
            for (int j = 0; j < relationsWithChildren.size(); j++) {
                CategoryRelation relation = new CategoryRelation();
                relation.setChild(relationsWithChildren.get(j).getChild());

                this.createCategoryRelation(
                        relationsWithChildren.get(j).getChild(),
                        relationsWithParents.get(i).getParent()
                );
            }
        }
    }
}
