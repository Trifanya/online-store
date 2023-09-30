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
import ru.devtrifanya.online_store.models.Feature;
import ru.devtrifanya.online_store.models.Searchable;
import ru.devtrifanya.online_store.services.CategoryService;
import ru.devtrifanya.online_store.services.FeatureService;
import ru.devtrifanya.online_store.services.ItemService;
import ru.devtrifanya.online_store.util.ErrorResponse;
import ru.devtrifanya.online_store.util.MainExceptionHandler;
import ru.devtrifanya.online_store.util.exceptions.InvalidDataException;
import ru.devtrifanya.online_store.util.validators.CategoryValidator;

import java.util.List;

@RestController
@RequestMapping("/categories/{categoryId}")
@Data
public class CategoryController {
    private final CategoryService categoryService;
    private final ItemService itemService;
    private final FeatureService featureService;
    private final CategoryValidator categoryValidator;
    private final ModelMapper modelMapper;
    private final MainExceptionHandler mainExceptionHandler;

    /**
     * Адрес: .../{categoryId}
     * Получение содержимого выбранной категории, для пользователей и администратора.
     * Данное действие происходит при нажатии на любую категорию товаров. При этом id выбранной категории
     * будет передан в параметрах запроса. Если выбранная категория содержит подкатегории товаров, то будет
     * возвращен список подкатегорий, если не содержит, то есть является конечной - то будет возвращен
     * список всех товаров данной категории. Вместе со списком товаров конечной категории будет возвращен
     * список характеристик данной категории. В параметрах запроса можно указать номер страницы, количество
     * товаров, отображаемых на одной странице и критерий сортировки.
     */
    @GetMapping
    public List<? extends Searchable> showContent(@PathVariable("categoryId") int categoryId,
                                                  @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
                                                  @RequestParam(value = "itemsPerPage", defaultValue = "10") int itemsPerPage,
                                                  @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {
        List<? extends Searchable> catalogElements = categoryService.getSubcategories(categoryId);
        if (catalogElements.size() == 0) {
            catalogElements = itemService.getAll(categoryId, pageNum, itemsPerPage, sortBy);
            List<Feature> categoryFeatures = featureService.getAll(categoryId);
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
        categoryValidator.validate(categoryDTO, parentId);
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

    @PatchMapping("/edit")
    public ResponseEntity<String> edit(@RequestBody @Valid CategoryDTO categoryDTO,
                                       @PathVariable("categoryId") int parentId,
                                       BindingResult bindingResult) {
        //categoryValidator.validate(categoryDTO, parentId);
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            StringBuilder errorMessage = new StringBuilder();
            for (FieldError error : errors) {
                errorMessage.append(error.getDefaultMessage() + "\n");
            }
            throw new InvalidDataException(errorMessage.toString());
        }
        categoryService.update(convertToCategory(categoryDTO), parentId);
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
