package ru.devtrifanya.online_store.rest.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.rest.dto.entities_dto.ItemImageDTO;
import ru.devtrifanya.online_store.rest.dto.entities_dto.ItemFeatureDTO;
import ru.devtrifanya.online_store.rest.dto.requests.AddItemRequest;
import ru.devtrifanya.online_store.rest.utils.MainClassConverter;
import ru.devtrifanya.online_store.rest.validators.ItemValidator;
import ru.devtrifanya.online_store.services.implementations.*;

import java.util.Map;

@RestController
@RequestMapping("/catalog/{categoryId}")
@RequiredArgsConstructor
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
    public ResponseEntity<?> createNewItem(@RequestBody @Valid AddItemRequest request) {
        itemValidator.validate(request);
        // сохранение товара
        Item createdItem = itemService.createNewItem(
                converter.convertToItem(request.getItem()),
                request.getCategoryId()
        );
        // сохранение характеристик товара
        for (Map.Entry<Integer, ItemFeatureDTO> itemFeature : request.getItemFeatures().entrySet()) {
            itemFeatureService.createNewItemFeature(
                    converter.convertToItemFeature(itemFeature.getValue()),
                    createdItem.getId(),
                    itemFeature.getKey()
            );
        } // сохранение изображений товара
        for (ItemImageDTO image : request.getItemImages()) {
            imageService.createNewImageIfNotExist(
                    converter.convertToImage(image),
                    createdItem.getId()
            );
        }
        return ResponseEntity.ok("Товар успешно добавлен.");
    }

    /**
     * Адрес: .../catalog/{categoryId}/{itemId}/updateItem
     * Обновление информации о товаре, только для администратора.
     */
    @PatchMapping("/{itemId}/updateItem")
    public ResponseEntity<?> updateItemInfo(@RequestBody @Valid AddItemRequest request) {
        itemValidator.validate(request);
        // обновление товара
        Item updatedItem = itemService.updateItemInfo(
                converter.convertToItem(request.getItem()),
                request.getCategoryId()
        );
        // обновление характеристик товара
        for (Map.Entry<Integer, ItemFeatureDTO> itemFeature : request.getItemFeatures().entrySet()) {
            itemFeatureService.updateItemFeatureInfo(
                    converter.convertToItemFeature(itemFeature.getValue()),
                    updatedItem.getId(),
                    itemFeature.getKey()
            );
        }
        for (ItemImageDTO image : request.getItemImages()) {
            imageService.createNewImageIfNotExist(
                    converter.convertToImage(image),
                    updatedItem.getId()
            );
        }
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

