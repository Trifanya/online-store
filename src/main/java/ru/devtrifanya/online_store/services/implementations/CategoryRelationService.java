package ru.devtrifanya.online_store.services.implementations;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
    private final CategoryService categoryService;

    private final CategoryRelationRepository relationRepository;

    public List<CategoryRelation> getRelationsByChildId(int childId) {
        return relationRepository.findAllByChildId(childId);
    }

    @Autowired
    public CategoryRelationService(@Lazy CategoryService categoryService,
                                   CategoryRelationRepository relationRepository) {
        this.categoryService = categoryService;
        this.relationRepository = relationRepository;
    }

    //public CategoryRelation createNewCategoryRelation(Category child, Category parent) {
    public CategoryRelation createNewCategoryRelation(int childId, int parentId) {
        Category child = categoryService.getCategory(childId);
        Category parent = categoryService.getCategory(parentId);

        CategoryRelation relationToSave = new CategoryRelation();
        relationToSave.setChild(child);
        relationToSave.setParent(parent);

        return relationRepository.save(relationToSave);
    }

    public void updateRelationsOfReplacedCategory(int categoryId, int newParentId) {
        if (relationRepository.existsByParentIdAndChildId(newParentId, categoryId)) {
            return; // Такое отношение уже существует, значит, категория не была перемещена.
        }
        Category newParent = categoryService.getCategory(newParentId);

        List<CategoryRelation> relationsWithParents = relationRepository.findAllByChildId(categoryId);
        for (CategoryRelation relationWithParent : relationsWithParents) {
            relationWithParent.setParent(newParent);
            relationRepository.save(relationWithParent);
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
                relation.setParent(relationsWithParents.get(i).getParent());
                relationRepository.save(relation);
            }
        }
    }
}
