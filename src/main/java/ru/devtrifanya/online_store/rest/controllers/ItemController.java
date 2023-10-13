package ru.devtrifanya.online_store.rest.controllers;

import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.rest.dto.requests.NewItemRequest;
import ru.devtrifanya.online_store.rest.dto.responses.ErrorResponse;
import ru.devtrifanya.online_store.services.*;
import ru.devtrifanya.online_store.rest.utils.MainClassConverter;
import ru.devtrifanya.online_store.rest.utils.MainExceptionHandler;
import ru.devtrifanya.online_store.rest.validators.ItemValidator;

@RestController
@RequestMapping("/catalog/{categoryId}")
@Data
public class ItemController {
    private final CategoryService categoryService;
    private final ItemService itemService;
    private final ItemFeatureService itemFeatureService;
    private final ReviewService reviewService;
    private final ItemValidator itemValidator;
    private final MainExceptionHandler exceptionHandler;
    private final MainClassConverter converter;

    /**
     * Адрес: .../catalog/{categoryId}/newItem
     * Добавление нового товара, только для администратора.
     */
    @PostMapping("/newItem")
    public ResponseEntity<?> createNewItem(@RequestBody @Valid NewItemRequest request) {
        itemValidator.validate(request);

        Item createdItem = itemService.createNewItem(
                converter.convertToItem(request.getItem()),
                request.getCategoryId()
        );
        return ResponseEntity.ok("Товар успешно добавлен.");
    }

    /**
     * Адрес: .../catalog/{categoryId}/{itemId}/updateItem
     * Обновление информации о товаре, только для администратора.
     */
    @PatchMapping("/{itemId}/updateItem")
    public ResponseEntity<?> updateItemInfo(@RequestBody @Valid NewItemRequest request) {
        itemValidator.validate(request);

        Item updatedItem = itemService.updateItemInfo(
                converter.convertToItem(request.getItem()),
                request.getCategoryId()
        );
        return ResponseEntity.ok("Информация о товаре успешно обновлена.");
    }

    /**
     * Адрес: .../catalog/{categoryId}/{itemId}/deleteItem
     * Удаление товара, только для администратора.
     */
    @DeleteMapping("/{itemId}/deleteItem")
    public ResponseEntity<String> deleteItem(@PathVariable("itemId") int itemId) {
        itemService.deleteItem(itemId);
        return ResponseEntity.ok("Товар успешно удален.");
    }
}

