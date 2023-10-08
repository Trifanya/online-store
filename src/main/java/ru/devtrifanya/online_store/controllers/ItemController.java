package ru.devtrifanya.online_store.controllers;

import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.dto.ItemDTO;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.services.CategoryService;
import ru.devtrifanya.online_store.services.FeatureService;
import ru.devtrifanya.online_store.services.ItemService;
import ru.devtrifanya.online_store.util.ErrorResponse;
import ru.devtrifanya.online_store.util.MainClassConverter;
import ru.devtrifanya.online_store.util.MainExceptionHandler;
import ru.devtrifanya.online_store.util.validators.ItemValidator;

@RestController
@RequestMapping("/categories/{categoryId}")
@Data
public class ItemController {
    private final ItemService itemService;
    private final FeatureService featureService;
    private final ItemValidator itemValidator;
    private final MainExceptionHandler exceptionHandler;
    private final MainClassConverter converter;


    /**
     * Адрес: .../{categories/{categoryId}/{itemId}}
     * Для пользователя и администратора.
     * Метод получает на вход id товара, затем вызывает метод сервиса для получения товара
     * по id и возвращает полученный товар, преобразованный в dto-объект.
     */
    @GetMapping("/{itemId}")
    public ItemDTO showItemInfo(@PathVariable("itemId") int itemId) {
        return converter.convertToItemDTO(
                itemService.getItem(itemId)
        );
    }

    /**
     * Адрес: .../{categoryId}/new
     * Добавление нового товара, только для администратора.
     * При добавлении нового товара кроме данных о самом товаре, которые хранятся в таблице Item,
     * нужно указать еще и значения характеристик, присущих товару (всем товарам в данной категории).
     * Значения характеристик будут храниться отдельно в таблице ItemCharacteristics.
     */
    @PostMapping("/newItem")
    public ResponseEntity<ItemDTO> addNewItem(@RequestBody @Valid ItemDTO itemDTO,
                                              @PathVariable("categoryId") int categoryId,
                                              BindingResult bindingResult) {
        itemValidator.validate(itemDTO, categoryId);
        if (bindingResult.hasErrors()) {
            exceptionHandler.throwInvalidDataException(bindingResult);
        }

        Item createdItem = itemService.createNewItem(
                converter.convertToItem(itemDTO),
                categoryId);

        return ResponseEntity.ok(converter.convertToItemDTO(createdItem));
    }

    @PatchMapping("/{itemId}/edit")
    public ResponseEntity<ItemDTO> updateItemInfo(@RequestBody @Valid ItemDTO itemDTO,
                                                 @PathVariable("itemId") int itemId,
                                                 @PathVariable("categoryId") int categoryId,
                                                 BindingResult bindingResult) {
        itemValidator.validate(itemDTO, categoryId);
        if (bindingResult.hasErrors()) {
            exceptionHandler.throwInvalidDataException(bindingResult);
        }

        Item updatedItem = itemService.updateItemInfo(
                itemId,
                converter.convertToItem(itemDTO),
                categoryId
        );

        return ResponseEntity.ok(converter.convertToItemDTO(updatedItem));
    }

    @DeleteMapping("/{itemId}/delete")
    public ResponseEntity<String> deleteItem(@PathVariable("itemId") int itemId) {
        itemService.deleteItem(itemId);
        return ResponseEntity.ok("Товар успешно удален.");
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        return exceptionHandler.handleException(exception);
    }
}

