package ru.devtrifanya.online_store.services;

import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ru.devtrifanya.online_store.models.Feature;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.exceptions.NotFoundException;
import ru.devtrifanya.online_store.repositories.CategoryRepository;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

@Service
public class CategoryService {
    private final FeatureService featureService;

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(@Lazy FeatureService featureService,
                           CategoryRepository categoryRepository) {
        this.featureService = featureService;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Получение категории по ее id.
     */
    public Category getCategory(int categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с указанным id не найдена."));
    }

    /**
     * Получение корневых категорий.
     */
    public List<Category> getRootCategories() {
        return categoryRepository.findRootCategories();
    }

    /**
     * Добавление новой категории.
     */
    public Category createNewCategory(Category categoryToSave, int[] featureIds, int parentId) {
        // Назначение характеристик добавляемой категории
        categoryToSave.setFeatures(new ArrayList<>());
        for (int i = 0; i < featureIds.length; i++) {
            Feature feature = featureService.getFeature(featureIds[i]);
            categoryToSave.getFeatures().add(feature);
        }
        // Создание нового отношения между добавляемой категорией и ее родительской категорией
        Category parent = getCategory(parentId);
        categoryToSave.setParents(Collections.singletonList(parent));

        return categoryRepository.save(categoryToSave);
    }

    @Transactional
    public Category updateCategoryInfo(Category updatedCategory, int[] featureIds, int prevParentId, int newParentId) {
        Category categoryToUpdate = getCategory(updatedCategory.getId());

        updatedCategory.setChildren(categoryToUpdate.getChildren());
        updatedCategory.setParents(categoryToUpdate.getParents());

        // Назначение характеристик обновляемой категории
        updatedCategory.setFeatures(new ArrayList<>());

        for (int i = 0; i < featureIds.length; i++) {
            Feature feature = featureService.getFeature(featureIds[i]);
            updatedCategory.getFeatures().add(feature);
        }
        // Если категория была перемещена в дереве категорий, то нужно обновить ее связи с другими категориями
        if (prevParentId != newParentId) {
            updatedCategory = updateRelationsOfReplacingCategory(updatedCategory, prevParentId, newParentId);
        }

        return categoryRepository.save(updatedCategory);
    }

    private Category updateRelationsOfReplacingCategory(Category updatedCategory, int prevParentId, int newParentId) {
        Category prevParent = getCategory(prevParentId);
        Category newParent = getCategory(newParentId);

        updatedCategory.getParents().remove(prevParent);
        //prevParent.getChildren().remove(updatedCategory);
        updatedCategory.getParents().add(newParent);
        //newParent.getChildren().add(updatedCategory);

        //categoryRepository.saveAll(List.of(prevParent, newParent));

        return updatedCategory;
    }

    /**
     * Удаление категории.
     */
    @Transactional
    public void deleteCategory(int categoryId) {
        if (!getCategory(categoryId).getChildren().isEmpty()) {
            updateRelationsOfRemovingCategory(categoryId);
        }
        categoryRepository.deleteById(categoryId);
    }

    /**
     * Изменение связей удаляемой категории.
     * Каждому родительской категории удаляемой категории добавляется связь с каждой
     * дочерней категорией удаляемой категории.
     */
    //@Transactional
    private void updateRelationsOfRemovingCategory(int categoryToDeleteId) {
        Category categoryToDelete = getCategory(categoryToDeleteId);

        List<Category> parents = categoryToDelete.getParents();
        List<Category> children = categoryToDelete.getChildren();

        for (int p = 0; p < parents.size(); p++) {
            parents.get(p).getChildren().remove(categoryToDelete);
            for (int ch = 0; ch < children.size(); ch++) {
                //children.get(ch).getParents().remove(categoryToDelete);
                parents.get(p).getChildren().add(children.get(ch));
                //children.get(ch).getParents().add(parents.get(p));
            }
        }
        categoryRepository.saveAll(parents);
        //categoryRepository.saveAll(children);
    }

}
