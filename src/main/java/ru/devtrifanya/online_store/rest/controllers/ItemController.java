package ru.devtrifanya.online_store.rest.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.rest.dto.entities_dto.ItemFeatureDTO;
import ru.devtrifanya.online_store.rest.dto.requests.AddOrUpdateItemRequest;
import ru.devtrifanya.online_store.rest.dto.requests.DeleteItemRequest;
import ru.devtrifanya.online_store.rest.utils.MainClassConverter;
import ru.devtrifanya.online_store.rest.validators.ItemValidator;
import ru.devtrifanya.online_store.services.ImageService;
import ru.devtrifanya.online_store.services.ItemFeatureService;
import ru.devtrifanya.online_store.services.ItemService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/catalog/{categoryId}")
public class ItemController {
    private final ItemService itemService;
    private final ItemFeatureService itemFeatureService;
    private final ImageService imageService;

    private final ItemValidator itemValidator;

    private final MainClassConverter converter;

    /**
     * Адрес: .../catalog/{categoryId}/newItem
     * Добавление нового товара, только для администратора.
     */
    @PostMapping("/newItem")
    public ResponseEntity<?> createNewItem(@RequestBody @Valid AddOrUpdateItemRequest request) {
        itemValidator.performNewItemValidation(request);

        // сохранение товара
        Item createdItem = itemService.createNewItem(
                converter.convertToItem(request.getItem()),
                request.getCategoryId()
        );
        // сохранение характеристик товара
        for (Map.Entry<Integer, ItemFeatureDTO> itemFeature : request.getItemFeatures().entrySet()) {
            itemFeatureService.createOrUpdateItemFeature(
                    converter.convertToItemFeature(itemFeature.getValue()),
                    createdItem.getId(),
                    itemFeature.getKey()
            );
        }
        // сохранение изображений товара
        request.getItemImages().forEach(
                image -> imageService.createNewImageIfNotExist(
                        converter.convertToImage(image),
                        createdItem.getId()
                ));

        return ResponseEntity.ok("Товар успешно добавлен.");
    }

    /**
     * Адрес: .../catalog/{categoryId}/{itemId}/updateItem
     * Обновление информации о товаре, только для администратора.
     */
    @PatchMapping("/{itemId}/updateItem")
    public ResponseEntity<?> updateItemInfo(@RequestBody @Valid AddOrUpdateItemRequest request) {
        itemValidator.performUpdatedItemValidation(request);

        // Обновление товара
        Item updatedItem = itemService.updateItemInfo(
                converter.convertToItem(request.getItem()),
                request.getCategoryId()
        );
        // Обновление характеристик товара
        for (Map.Entry<Integer, ItemFeatureDTO> itemFeature : request.getItemFeatures().entrySet()) {
            itemFeatureService.createOrUpdateItemFeature(
                    converter.convertToItemFeature(itemFeature.getValue()),
                    updatedItem.getId(),
                    itemFeature.getKey()
            );
        }
        // Обновление изображений товара
        request.getItemImages().forEach(
                image -> imageService.createNewImageIfNotExist(
                        converter.convertToImage(image),
                        updatedItem.getId()
                ));

        return ResponseEntity.ok("Информация о товаре успешно обновлена.");
    }

    /**
     * Адрес: .../catalog/{categoryId}/{itemId}/deleteItem
     * Удаление товара, только для администратора.
     */
    @DeleteMapping({
            "/deleteItem",
            "/{itemId}/deleteItem"
    })
    public ResponseEntity<String> deleteItem(@RequestBody @Valid DeleteItemRequest request) {
        itemService.deleteItem(request.getItemToDeleteId());
        return ResponseEntity.ok("Товар успешно удален.");
    }
}

