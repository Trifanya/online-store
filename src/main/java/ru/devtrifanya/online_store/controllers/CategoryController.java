package ru.devtrifanya.online_store.controllers;

import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.content.dto.CategoryDTO;
import ru.devtrifanya.online_store.content.pages.ItemsPage;
import ru.devtrifanya.online_store.content.pages.MainPage;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.models.Feature;
import ru.devtrifanya.online_store.services.CartElementService;
import ru.devtrifanya.online_store.services.CategoryService;
import ru.devtrifanya.online_store.services.FeatureService;
import ru.devtrifanya.online_store.services.ItemService;
import ru.devtrifanya.online_store.util.ErrorResponse;
import ru.devtrifanya.online_store.util.MainClassConverter;
import ru.devtrifanya.online_store.util.MainExceptionHandler;
import ru.devtrifanya.online_store.util.validators.CategoryValidator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
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
     * Адрес: .../categories/{categoryId}/new
     * Только для администратора.
     * Добавление новой категории товаров.
     */
    @PostMapping("/{categoryId}/newCategory")
    public ResponseEntity<CategoryDTO> createNewCategory(@RequestBody @Valid CategoryDTO categoryDTO,
                                                         @PathVariable("categoryId") int parentId,
                                                         BindingResult bindingResult) {
        categoryValidator.validate(categoryDTO, parentId);
        if (bindingResult.hasErrors()) {
            exceptionHandler.throwInvalidDataException(bindingResult);
        }

        List<Feature> categoryFeature = null;

        Category savedCategory = categoryService.createNewCategory(
                converter.convertToCategory(categoryDTO),
                categoryFeature
        );

        return ResponseEntity.ok(converter.convertToCategoryDTO(savedCategory));
    }

    @PatchMapping("/{categoryId}/editCategory")
    public ResponseEntity<CategoryDTO> updateCategoryInfo(@RequestBody @Valid CategoryDTO categoryDTO,
                                                          @PathVariable("categoryId") int categoryId,
                                                          BindingResult bindingResult) {
        categoryValidator.validate(categoryDTO, categoryId);
        if (bindingResult.hasErrors()) {
            exceptionHandler.throwInvalidDataException(bindingResult);
        }

        List<Feature> categoryFeatures = null;

        Category updatedCategory = categoryService.updateCategory(
                categoryId,
                converter.convertToCategory(categoryDTO),
                categoryFeatures
        );

        return ResponseEntity.ok(converter.convertToCategoryDTO(updatedCategory));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteCategory(@PathVariable("categoryId") int categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok("Категория успешно удалена.");

    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        return exceptionHandler.handleException(exception);
    }
}
