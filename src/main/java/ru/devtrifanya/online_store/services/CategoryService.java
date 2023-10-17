package ru.devtrifanya.online_store.services;

import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ru.devtrifanya.online_store.models.Feature;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.exceptions.NotFoundException;
import ru.devtrifanya.online_store.repositories.CategoryRepository;
import ru.devtrifanya.online_store.repositories.CategoryRelationRepository;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final FeatureService featureService;
    private final CategoryRelationService categoryRelationService;

    private final CategoryRepository categoryRepository;
    private final CategoryRelationRepository categoryRelationRepository;

    @Autowired
    public CategoryService(@Lazy FeatureService featureService, @Lazy CategoryRelationService categoryRelationService,
                           CategoryRepository categoryRepository, CategoryRelationRepository categoryRelationRepository) {
        this.featureService = featureService;
        this.categoryRelationService = categoryRelationService;
        this.categoryRepository = categoryRepository;
        this.categoryRelationRepository = categoryRelationRepository;
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
        return categoryRelationRepository.findAllByParentIdIsNull()
                .stream()
                .map(relation -> relation.getChild())
                .collect(Collectors.toList());
    }

    /**
     * Добавление новой категории.
     */
    public Category createOrUpdateCategory(Category categoryToSave, int[] featuresId) {
        categoryToSave.setFeatures(new ArrayList<>());
        for (int i = 0; i < featuresId.length; i++) {
            Feature feature = featureService.getFeature(featuresId[i]);
            categoryToSave.getFeatures().add(feature);
        }
        return categoryRepository.save(categoryToSave);
    }

    /**
     * Удаление категории.
     */
    @Transactional
    public void deleteCategory(int categoryId) {
        categoryRelationService.updateRelationsOfDeletingCategory(categoryId);
        categoryRepository.deleteById(categoryId);
    }

}
