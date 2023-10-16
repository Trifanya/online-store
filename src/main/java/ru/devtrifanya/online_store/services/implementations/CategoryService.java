package ru.devtrifanya.online_store.services.implementations;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.models.Feature;
import ru.devtrifanya.online_store.repositories.CategoryRelationRepository;
import ru.devtrifanya.online_store.repositories.CategoryRepository;
import ru.devtrifanya.online_store.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Data
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
     * Метод получает на вход id категории, затем вызывает метод репозитория для получения
     * категории по id и возвращает найденную категорию.
     * Если категория с указанным id не найдена в БД, то выбрасывается исключение.
     */
    public Category getCategory(int categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с указанным id не найдена."));
    }

    public List<Category> getTopCategories() {
        return categoryRelationRepository.findAllByParentIdIsNull()
                .stream()
                .map(relation -> relation.getChild())
                .collect(Collectors.toList());
    }

    @Transactional
    public Category createNewCategory(Category categoryToSave, int[] featuresId) {
        categoryToSave.setId(0);
        categoryToSave.setFeatures(new ArrayList<>());
        for (int i = 0; i < featuresId.length; i++) {
            Feature feature = featureService.getFeature(featuresId[i]);
            categoryToSave.getFeatures().add(feature);
        }
        return categoryRepository.save(categoryToSave);
    }


    @Transactional
    public Category updateCategoryInfo(Category updatedCategory, int[] featuresId) {
        updatedCategory.setFeatures(new ArrayList<>());
        for (int i = 0; i < featuresId.length; i++) {
            Feature feature = featureService.getFeature(featuresId[i]);
            updatedCategory.getFeatures().add(feature);
        }
        return categoryRepository.save(updatedCategory);
    }


    @Transactional
    public void deleteCategory(int categoryId) {
        categoryRelationService.updateRelationsOfDeletingCategory(categoryId);
        categoryRepository.deleteById(categoryId);
    }

}
