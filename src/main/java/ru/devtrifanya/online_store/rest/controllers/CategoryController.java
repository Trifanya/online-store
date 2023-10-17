package ru.devtrifanya.online_store.rest.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.rest.dto.requests.AddOrUpdateCategoryRequest;
import ru.devtrifanya.online_store.rest.utils.MainClassConverter;
import ru.devtrifanya.online_store.rest.validators.CategoryValidator;
import ru.devtrifanya.online_store.rest.validators.FeatureValidator;
import ru.devtrifanya.online_store.services.CategoryRelationService;
import ru.devtrifanya.online_store.services.CategoryService;
import ru.devtrifanya.online_store.services.FeatureService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/catalog/{categoryId}")
public class CategoryController {
    private final FeatureService featureService;
    private final CategoryService categoryService;
    private final CategoryRelationService categoryRelationService;

    private final FeatureValidator featureValidator;
    private final CategoryValidator categoryValidator;

    private final MainClassConverter converter;

    /**
     * Адрес: .../catalog/{categoryId}/newCategory
     * Добавление новой категории товаров, только для администратора.
     */
    @PostMapping("/newCategory")
    public ResponseEntity<?> createNewCategory(@RequestBody @Valid AddOrUpdateCategoryRequest request) {
        categoryValidator.validate(request);

        // Сохранение новой категории
        Category createdCategory = categoryService.createOrUpdateCategory(
                converter.convertToCategory(request.getCategory()),
                request.getExistingFeaturesId()
        );
        // Создание связи между новой категорией и ее родительской категорией
        categoryRelationService.createNewCategoryRelation(
                createdCategory.getId(),
                request.getParentCategoryId()
        );
        // Сохранение новых характеристик, если такие есть
        request.getNewFeatures().stream()
                .forEach(featureDTO -> {
                    featureValidator.validate(featureDTO);
                    featureService.createNewFeature(
                            converter.convertToFeature(featureDTO),
                            createdCategory.getId()
                    );
                });

        return ResponseEntity.ok("Категория успешно добавлена в дерево категорий.");
    }

    /**
     * Адрес: .../catalog/{categoryId}/updateCategory
     * Обновление информации о категории товаров, только для администратора.
     */
    @PatchMapping("/updateCategory")
    public ResponseEntity<?> updateCategoryInfo(@RequestBody @Valid AddOrUpdateCategoryRequest request) {
        categoryValidator.validate(request);

        // Апдейт информации о категории
        Category updatedCategory = categoryService.createOrUpdateCategory(
                converter.convertToCategory(request.getCategory()),
                request.getExistingFeaturesId()
        );
        // Апдейт связей между категориями, если категория была перемещена
        categoryRelationService.updateRelationsOfReplacedCategory(
                updatedCategory.getId(),
                request.getParentCategoryId()
        );
        // Сохранение новых характеристик, если такие есть
        request.getNewFeatures().stream()
                .forEach(featureDTO -> {
                    featureValidator.validate(featureDTO);
                    featureService.createNewFeature(
                            converter.convertToFeature(featureDTO),
                            updatedCategory.getId()
                    );
                });

        return ResponseEntity.ok("Информация о категории успешно обновлена.");
    }

    /**
     * Адрес: .../catalog/{categoryId}/deleteCategory
     * Удаление категории товаров, только для администратора.
     */
    @DeleteMapping("/deleteCategory")
    public ResponseEntity<String> deleteCategory(@PathVariable("categoryId") int categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok("Категория успешно удалена.");
    }
}
