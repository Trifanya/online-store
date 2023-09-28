package ru.devtrifanya.online_store.controllers;

import jakarta.validation.Valid;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.dto.CategoryDTO;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.models.Searchable;
import ru.devtrifanya.online_store.services.CategoryService;
import ru.devtrifanya.online_store.services.ItemService;
import ru.devtrifanya.online_store.util.ErrorResponse;
import ru.devtrifanya.online_store.util.MainExceptionHandler;
import ru.devtrifanya.online_store.util.exceptions.InvalidDataException;
import ru.devtrifanya.online_store.util.validators.CategoryValidator;

import java.util.List;

@RestController
@RequestMapping("/{categoryId}")
@Data
public class CategoryController {
    private final CategoryService categoryService;
    private final ItemService itemService;
    private final CategoryValidator categoryValidator;
    private final ModelMapper modelMapper;
    private final MainExceptionHandler mainExceptionHandler;

    /**
     * Адрес: .../{categoryId}
     * Получение товаров или подкатегорий какой-то категории, для пользователей и администратора.
     * Данное действие происходит при нажатии на любую категорию товаров. При этом id выбранной категории
     * будет передан в параметрах запроса. Если выбранная категория содержит подкатегории товаров, то будет
     * возвращен список подкатегорий, если не содержит, то будет возвращен список всех товаров данной категории.
     */
    @GetMapping
    public List<? extends Searchable> showSubcategoriesOrItems(@PathVariable("categoryId") int categoryId) {
        List<? extends Searchable> catalogElements = categoryService.getAll(categoryId);
        if (catalogElements.size() == 0) {
            catalogElements = itemService.getAll(categoryId);
        }
        return catalogElements;
    }

    /**
     * .../categories/{categoryId}/new - добавление новой категории товаров, только для администратора;
     * Для добавления новой категории товаров нужно перейти в родительскую категорию, в которой будет создана
     * новая категория. ID этой родительской категориии будет передан в параметрах запроса "categoryId" для
     * создания связи parent-child в таблице CategoryRelation.
     */
    @PostMapping("/newCategory")
    public ResponseEntity<String> add(@RequestBody @Valid CategoryDTO categoryDTO,
                                      @PathVariable("categoryId") int parentId,
                                      BindingResult bindingResult) {
        //categoryValidator.validate(categoryDTO);
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            StringBuilder errorMessage = new StringBuilder();
            for (FieldError error : errors) {
                errorMessage.append(error.getDefaultMessage() + "\n");
            }
            throw new InvalidDataException(errorMessage.toString());
        }
        categoryService.create(convertToCategory(categoryDTO), parentId);
        return ResponseEntity.ok("Категория успешно добавлена.");
    }

    @PatchMapping("/editCategory")
    public ResponseEntity<String> edit(@RequestBody @Valid CategoryDTO categoryDTO,
                                       @PathVariable("categoryId") int parentId,
                                       BindingResult bindingResult) {
        //categoryValidator.validate(categoryDTO);
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            StringBuilder errorMessage = new StringBuilder();
            for (FieldError error : errors) {
                errorMessage.append(error.getDefaultMessage() + "\n");
            }
            throw new InvalidDataException(errorMessage.toString());
        }
        categoryService.create(convertToCategory(categoryDTO), parentId);
        return ResponseEntity.ok("Категория успешно изменена.");

    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@PathVariable("categoryId") int categoryId) {
        // TODO
        return ResponseEntity.ok("Категория успешно удалена.");

    }

    public Category convertToCategory(CategoryDTO categoryDTO) {
        return modelMapper.map(categoryDTO, Category.class);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        return mainExceptionHandler.handleException(exception);
    }
}
