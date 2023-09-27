package ru.devtrifanya.online_store.controllers;

import jakarta.validation.Valid;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.dto.CategoryDTO;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.models.Searchable;
import ru.devtrifanya.online_store.services.CategoryService;
import ru.devtrifanya.online_store.services.ItemService;
import ru.devtrifanya.online_store.util.errorResponses.ErrorResponse;
import ru.devtrifanya.online_store.util.exceptions.category.InvalidCategoryDataException;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Data
public class CategoryController {
    private final CategoryService categoryService;
    private final ItemService itemService;
    private final ModelMapper modelMapper;

    /**
     * Адрес: .../categories/{categoryId}
     * Получение товаров или подкатегорий какой-то категории, для пользователей и администратора.
     * Данное действие происходит при нажатии на любую категорию товаров. При этом id выбранной категории
     * будет передан в параметрах запроса. Если выбранная категория содержит подкатегории товаров, то будет
     * возвращен список подкатегорий, если не содержит, то будет возвращен список всех товаров данной категории.
     */
    @GetMapping("/{categoryId}")
    public List<? extends Searchable> showSubcategoriesOrItems(@PathVariable("categoryId") int id) {
        List<? extends Searchable> catalogElements = categoryService.getSubcategories(id);
        if (catalogElements.size() == 0) {
            catalogElements = itemService.getItemsOfCategory(id);
        }
        return catalogElements;
    }

    /**
     * .../categories/{categoryId}/new - добавление новой категории товаров, только для администратора;
     * Для добавления новой категории товаров нужно перейти в родительскую категорию, в которой будет создана
     * новая категория. ID этой родительской категориии будет передан в параметрах запроса "categoryId" для
     * создания связи parent-child в таблице CategoryRelation.
     */
    @PostMapping("/{categoryId}/new")
    public ResponseEntity<String> addCategory(@RequestBody @Valid CategoryDTO categoryDTO,
                                              @PathVariable("categoryId") int categoryId,
                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            StringBuilder errorMessage = new StringBuilder();
            for (FieldError error : errors) {
                errorMessage.append(error.getDefaultMessage() + "\n");
            }
            throw new InvalidCategoryDataException(errorMessage.toString());
        }
        categoryService.createCategory(convertToCategory(categoryDTO), categoryId);
        return new ResponseEntity<>("Категория успешно добавлена.", HttpStatus.CREATED);
    }

    public Category convertToCategory(CategoryDTO categoryDTO) {
        return modelMapper.map(categoryDTO, Category.class);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        String errorMessage = null;
        HttpStatus status = null;
        if (exception instanceof InvalidCategoryDataException) {
            status = HttpStatus.BAD_REQUEST;
        }
        ErrorResponse response = new ErrorResponse(exception.getMessage());
        return new ResponseEntity<>(response, status);
    }
}