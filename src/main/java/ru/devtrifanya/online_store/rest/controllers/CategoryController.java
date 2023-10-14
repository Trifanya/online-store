package ru.devtrifanya.online_store.rest.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.rest.dto.requests.NewCategoryRequest;
import ru.devtrifanya.online_store.rest.utils.MainClassConverter;
import ru.devtrifanya.online_store.rest.validators.CategoryValidator;
import ru.devtrifanya.online_store.services.implementations.*;

@RestController
@RequestMapping("/catalog/{categoryId}")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryRelationService categoryRelationService;

    private final CategoryValidator categoryValidator;

    private final MainClassConverter converter;

    /**
     * Адрес: 1) .../catalog/{categoryId}/newCategory
     * Добавление новой категории товаров, только для администратора.
     */
    @PostMapping("/newCategory")
    public ResponseEntity<?> createNewCategory(@RequestBody @Valid NewCategoryRequest request) {
        categoryValidator.validate(request);

        Category createdCategory = categoryService.createNewCategory(
                converter.convertToCategory(request.getCategory()),
                request.getFeaturesId()
        );
        categoryRelationService.createNewCategoryRelation(
                createdCategory.getId(),
                request.getParentCategoryId()
        );

        return ResponseEntity.ok("Категория успешно добавлена в дерево категорий.");
    }

    /**
     * Адрес: 1) .../catalog/{categoryId}/updateCategory
     * Обновление информации о категории товаров, только для администратора.
     */
    @PatchMapping("/updateCategory")
    public ResponseEntity<?> updateCategoryInfo(@RequestBody @Valid NewCategoryRequest request) {
        categoryValidator.validate(request);

        Category updatedCategory = categoryService.updateCategoryInfo(
                converter.convertToCategory(request.getCategory()),
                request.getFeaturesId()
        );
        categoryRelationService.updateRelationsOfReplacedCategory(
                updatedCategory.getId(),
                request.getParentCategoryId()
        );

        return ResponseEntity.ok("Информация о категории успешно обновлена.");
    }

    /**
     * Адрес: 1) .../catalog/{categoryId}/deleteCategory
     * Удаление категории товаров, только для администратора.
     */
    @DeleteMapping("/deleteCategory")
    public ResponseEntity<String> deleteCategory(@PathVariable("categoryId") int categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok("Категория успешно удалена.");

    }
}
