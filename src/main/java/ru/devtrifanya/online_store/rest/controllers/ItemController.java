package ru.devtrifanya.online_store.rest.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.services.ItemService;
import ru.devtrifanya.online_store.services.ImageService;
import ru.devtrifanya.online_store.services.ItemFeatureService;
import ru.devtrifanya.online_store.rest.validators.ItemValidator;
import ru.devtrifanya.online_store.rest.utils.MainClassConverter;
import ru.devtrifanya.online_store.rest.dto.entities_dto.ItemFeatureDTO;
import ru.devtrifanya.online_store.rest.dto.requests.AddOrUpdateItemRequest;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final ImageService imageService;
    private final ItemFeatureService itemFeatureService;

    private final ItemValidator itemValidator;

    private final MainClassConverter converter;

    /**
     * Адрес: .../items/newItem
     * Добавление нового товара, только для администратора.
     */
    @PostMapping("/newItem")
    public ResponseEntity<?> createNewItem(@RequestBody @Valid AddOrUpdateItemRequest request) {
        itemValidator.performNewItemValidation(request);

        // Сохранение товара
        Item createdItem = itemService.createNewItem(
                converter.convertToItem(request.getItem()),
                request.getCategoryId()
        );
        // Сохранение характеристик товара
        for (Map.Entry<Integer, ItemFeatureDTO> itemFeature : request.getItemFeatures().entrySet()) {
            itemFeatureService.createOrUpdateItemFeature(
                    converter.convertToItemFeature(itemFeature.getValue()),
                    createdItem.getId(),
                    itemFeature.getKey()
            );
        }
        // Сохранение изображений товара
        request.getItemImages().forEach(
                image -> imageService.createNewImageIfNotExist(
                        converter.convertToImage(image),
                        createdItem.getId()
                ));

        return ResponseEntity.ok("Товар успешно добавлен.");
    }

    /**
     * Адрес: .../items/updateItem
     * Обновление информации о товаре, только для администратора.
     */
    @PatchMapping("/updateItem")
    public ResponseEntity<?> updateItem(@RequestBody @Valid AddOrUpdateItemRequest request) {
        itemValidator.performUpdatedItemValidation(request);

        // Обновление товара
        Item updatedItem = itemService.updateItem(
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
     * Адрес: .../items/{itemId}/deleteItem
     * Удаление товара, только для администратора.
     */
    @DeleteMapping("/{itemId}/deleteItem")
    public ResponseEntity<String> deleteItem(@PathVariable("itemId") int itemToDeleteId) {
        itemService.deleteItem(itemToDeleteId);
        return ResponseEntity.ok("Товар успешно удален.");
    }
}

