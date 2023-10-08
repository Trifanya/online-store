package ru.devtrifanya.online_store.controllers;

import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.dto.CatalogableDTO;
import ru.devtrifanya.online_store.dto.CategoryDTO;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.services.CategoryService;
import ru.devtrifanya.online_store.services.ItemService;
import ru.devtrifanya.online_store.util.ErrorResponse;
import ru.devtrifanya.online_store.util.MainClassConverter;
import ru.devtrifanya.online_store.util.MainExceptionHandler;
import ru.devtrifanya.online_store.util.validators.CategoryValidator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories/{categoryId}")
@Data
public class CategoryController {
    private final CategoryService categoryService;
    private final ItemService itemService;
    private final CategoryValidator categoryValidator;
    private final MainExceptionHandler exceptionHandler;
    private final MainClassConverter converter;

    /**
     * Адрес: .../categories/{categoryId}
     * Для пользователя и администратора.
     * Получение по id dto-объекта категории со списком товаров, заданным параметрами
     * запроса, если у категории есть товары, и с пустым списком товаров, если категория
     * является промежуточной.
     */
    @GetMapping
    public CategoryDTO getCategory(@PathVariable("categoryId") int categoryId,
                                   @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                                   @RequestParam(name = "itemsPerPage", defaultValue = "10") int itemsPerPage,
                                   @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
                                   @RequestParam Map<String, String> allParams) {
        return converter.convertToCategoryDTO(
                categoryService.getCategory(
                        categoryId,
                        pageNumber,
                        itemsPerPage,
                        sortBy,
                        allParams)
        );
    }

    /**
     * Адрес: .../categories/{categoryId}/new
     * Только для администратора.
     * Добавление новой категории товаров,
     * Для добавления новой категории товаров нужно перейти в родительскую категорию, в которой будет создана
     * новая категория. ID этой родительской категориии будет передан в параметрах запроса "categoryId" для
     * создания связи parent-child в таблице CategoryRelation.
     */
    @PostMapping("/newCategory")
    public ResponseEntity<CategoryDTO> createNewCategory(@RequestBody @Valid CategoryDTO categoryDTO,
                                                         @PathVariable("categoryId") int parentId,
                                                         BindingResult bindingResult) {
        categoryValidator.validate(categoryDTO, parentId);
        if (bindingResult.hasErrors()) {
            exceptionHandler.throwInvalidDataException(bindingResult);
        }

        Category savedCategory = categoryService.createNewCategory(
                converter.convertToCategory(categoryDTO),
                parentId
        );

        return ResponseEntity.ok(converter.convertToCategoryDTO(savedCategory));
    }

    @PatchMapping("/editCategory")
    public ResponseEntity<CategoryDTO> updateCategoryInfo(@RequestBody @Valid CategoryDTO categoryDTO,
                                                     @PathVariable("categoryId") int categoryId,
                                                     BindingResult bindingResult) {
        categoryValidator.validate(categoryDTO, categoryId);
        if (bindingResult.hasErrors()) {
            exceptionHandler.throwInvalidDataException(bindingResult);
        }

        Category updatedCategory = categoryService.updateCategory(
                categoryId,
                converter.convertToCategory(categoryDTO)
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
