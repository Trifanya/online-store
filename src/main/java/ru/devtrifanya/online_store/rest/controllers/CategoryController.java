package ru.devtrifanya.online_store.rest.controllers;

import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.rest.dto.requests.NewCategoryRequest;
import ru.devtrifanya.online_store.rest.dto.responses.ErrorResponse;
import ru.devtrifanya.online_store.services.CartElementService;
import ru.devtrifanya.online_store.services.CategoryService;
import ru.devtrifanya.online_store.services.FeatureService;
import ru.devtrifanya.online_store.services.ItemService;
import ru.devtrifanya.online_store.util.MainClassConverter;
import ru.devtrifanya.online_store.util.MainExceptionHandler;
import ru.devtrifanya.online_store.rest.validators.CategoryValidator;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog/{categoryId}")
@Data
public class CategoryController {
    private final CategoryService categoryService;
    private final ItemService itemService;
    private final FeatureService featureService;
    private final CartElementService cartElementService;
    private final CategoryValidator categoryValidator;
    private final MainExceptionHandler exceptionHandler;
    private final MainClassConverter converter;

    /**
     * Адрес: 1) .../catalog/{categoryId}/newCategory
     * Добавление новой категории товаров, только для администратора.
     */
    @PostMapping("/newCategory")
    public ResponseEntity<?> createNewCategory(@RequestBody @Valid NewCategoryRequest request,
                                               BindingResult bindingResult) {
        categoryValidator.validate(request);
        if (bindingResult.hasErrors()) {
            exceptionHandler.throwInvalidDataException(bindingResult);
        }
        Category savedCategory = categoryService.createNewCategory(
                request.getParentCategoryId(),
                converter.convertToCategory(request.getCategory()),
                request.getFeatures()
                        .stream()
                        .map(featureDTO -> converter.convertToFeature(featureDTO))
                        .collect(Collectors.toList())
        );
        return ResponseEntity.ok("Категория успешно добавлена в дерево категорий.");
    }

    /**
     * Адрес: 1) .../catalog/{categoryId}/updateCategory
     * Обновление информации о категории товаров, только для администратора.
     */
    @PatchMapping("/updateCategory")
    public ResponseEntity<?> updateCategoryInfo(@RequestBody @Valid NewCategoryRequest request,
                                                BindingResult bindingResult) {
        categoryValidator.validate(request);
        if (bindingResult.hasErrors()) {
            exceptionHandler.throwInvalidDataException(bindingResult);
        }
        Category updatedCategory = categoryService.updateCategory(
                converter.convertToCategory(request.getCategory()),
                request.getFeatures()
                        .stream()
                        .map(featureDTO -> converter.convertToFeature(featureDTO))
                        .collect(Collectors.toList())
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

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        return exceptionHandler.handleException(exception);
    }
}
