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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/{categoryId}")
@Data
public class CategoryController {
    private final CategoryService categoryService;
    private final ItemService itemService;
    private final CategoryValidator categoryValidator;
    private final MainExceptionHandler exceptionHandler;
    private final MainClassConverter converter;

    /**
     * Адрес: .../{categoryId}
     * Получение товаров или подкатегорий какой-то категории, для пользователей и администратора.
     * Данное действие происходит при нажатии на любую категорию товаров. При этом id выбранной категории
     * будет передан в параметрах запроса. Если выбранная категория содержит подкатегории товаров, то будет
     * возвращен список подкатегорий, если не содержит, то будет возвращен список всех товаров данной категории.
     */
    @GetMapping
    public List<? extends CatalogableDTO> showCategoryContent(@PathVariable("categoryId") int categoryId,
                                                              @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                                                              @RequestParam(name = "itemsPerPage", defaultValue = "10") int itemsPerPage,
                                                              @RequestParam(name = "sortBy", defaultValue = "id") String sortBy) {
        List<Category> subcategories = categoryService.getSubcategories(categoryId);

        if (subcategories.isEmpty()) {
            return itemService.getItemsByCategory(categoryId, pageNumber, itemsPerPage, "sortby")
                    .stream()
                    .map(item -> converter.convertToItemDTO(item))
                    .collect(Collectors.toList());
        }
        return subcategories
                .stream()
                .map(category -> converter.convertToCategoryDTO(category))
                .collect(Collectors.toList());
    }

    /**
     * .../categories/{categoryId}/new - добавление новой категории товаров, только для администратора;
     * Для добавления новой категории товаров нужно перейти в родительскую категорию, в которой будет создана
     * новая категория. ID этой родительской категориии будет передан в параметрах запроса "categoryId" для
     * создания связи parent-child в таблице CategoryRelation.
     */
    @PostMapping("/newCategory")
    public ResponseEntity<String> addNewCategory(@RequestBody @Valid CategoryDTO categoryDTO,
                                      @PathVariable("categoryId") int parentId,
                                      BindingResult bindingResult) {
        categoryValidator.validate(categoryDTO, parentId);
        if (bindingResult.hasErrors()) {
            exceptionHandler.throwInvalidDataException(bindingResult);
        }
        categoryService.createNewCategory(converter.convertToCategory(categoryDTO), parentId);

        return ResponseEntity.ok("Категория успешно добавлена.");
    }

    @PatchMapping("/editCategory")
    public ResponseEntity<String> editCategoryInfo(@RequestBody @Valid CategoryDTO categoryDTO,
                                       @PathVariable("categoryId") int parentId,
                                       BindingResult bindingResult) {
        categoryValidator.validate(categoryDTO, parentId);
        if (bindingResult.hasErrors()) {
            exceptionHandler.throwInvalidDataException(bindingResult);
        }

        categoryService.createNewCategory(converter.convertToCategory(categoryDTO), parentId);

        return ResponseEntity.ok("Категория успешно изменена.");
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
