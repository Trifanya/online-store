package ru.devtrifanya.online_store.rest.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.rest.dto.requests.AddOrUpdateCategoryRequest;
import ru.devtrifanya.online_store.rest.dto.requests.DeleteCategoryRequest;
import ru.devtrifanya.online_store.rest.utils.MainClassConverter;
import ru.devtrifanya.online_store.rest.validators.CategoryValidator;
import ru.devtrifanya.online_store.rest.validators.FeatureValidator;
import ru.devtrifanya.online_store.services.CategoryService;
import ru.devtrifanya.online_store.services.FeatureService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/catalog")
public class CategoryController {
    private final FeatureService featureService;
    private final CategoryService categoryService;

    private final FeatureValidator featureValidator;
    private final CategoryValidator categoryValidator;

    private final MainClassConverter converter;

    /**
     * Адрес: .../catalog/{categoryId}/newCategory
     * Добавление новой категории товаров, только для администратора.
     */
    @PostMapping("/{categoryId}/newCategory")
    public ResponseEntity<?> createNewCategory(@RequestBody @Valid AddOrUpdateCategoryRequest request) {
        categoryValidator.performNewCategoryValidation(request);

        // Сохранение новой категории
        Category createdCategory = categoryService.createNewCategory(
                converter.convertToCategory(request.getCategory()),
                request.getExistingFeaturesId(),
                request.getNewParentId()
        );
        // Сохранение новых характеристик, если такие есть
        request.getNewFeatures().forEach(
                featureDTO -> featureService.createNewFeature(
                        converter.convertToFeature(featureDTO),
                        createdCategory.getId()
                ));


        return ResponseEntity.ok("Категория успешно добавлена в дерево категорий.");
    }

    /**
     * Адрес: .../catalog/{categoryId}/updateCategory
     * Обновление информации о категории товаров, только для администратора.
     */
    @PatchMapping("/{categoryId}/updateCategory")
    public ResponseEntity<?> updateCategoryInfo(@RequestBody @Valid AddOrUpdateCategoryRequest request) {
        categoryValidator.performUpdatedCategoryValidation(request);

        // Апдейт информации о категории
        Category updatedCategory = categoryService.updateCategory(
                converter.convertToCategory(request.getCategory()),
                request.getExistingFeaturesId(),
                request.getPrevParentId(),
                request.getNewParentId()
        );
        // Сохранение новых характеристик, если такие есть
        request.getNewFeatures().forEach(
                featureDTO -> featureService.createNewFeature(
                        converter.convertToFeature(featureDTO),
                        updatedCategory.getId()
                ));

        return ResponseEntity.ok("Информация о категории успешно обновлена.");
    }

    /**
     * Адрес: .../catalog/deleteCategory
     * Удаление категории товаров, только для администратора.
     */
    @DeleteMapping("/deleteCategory")
    public ResponseEntity<String> deleteCategory(@RequestBody @Valid DeleteCategoryRequest request) {
        categoryService.deleteCategory(request.getCategoryToDeleteId());
        return ResponseEntity.ok("Категория успешно удалена.");
    }
}
