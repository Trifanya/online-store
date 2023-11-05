package ru.devtrifanya.online_store.rest.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.services.FeatureService;
import ru.devtrifanya.online_store.services.CategoryService;
import ru.devtrifanya.online_store.rest.utils.MainClassConverter;
import ru.devtrifanya.online_store.rest.validators.CategoryValidator;
import ru.devtrifanya.online_store.rest.dto.requests.AddOrUpdateCategoryRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final FeatureService featureService;
    private final CategoryService categoryService;

    private final CategoryValidator categoryValidator;

    private final MainClassConverter converter;

    /**
     * Адрес: .../categories/newCategory
     * Добавление новой категории товаров, только для администратора.
     */
    @PostMapping("/newCategory")
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
     * Адрес: .../categories/updateCategory
     * Обновление категории товаров, только для администратора.
     */
    @PatchMapping("/updateCategory")
    public ResponseEntity<?> updateCategory(@RequestBody @Valid AddOrUpdateCategoryRequest request) {
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
     * Адрес: .../categories/{categoryId}/deleteCategory
     * Удаление категории товаров, только для администратора.
     */
    @DeleteMapping("/{categoryId}/deleteCategory")
    public ResponseEntity<String> deleteCategory(@PathVariable("categoryId") int categoryToDeleteId) {
        categoryService.deleteCategory(categoryToDeleteId);
        return ResponseEntity.ok("Категория успешно удалена.");
    }
}
