package ru.devtrifanya.online_store.rest.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.ItemFeature;
import ru.devtrifanya.online_store.services.ItemService;
import ru.devtrifanya.online_store.services.ImageService;
import ru.devtrifanya.online_store.services.ItemFeatureService;
import ru.devtrifanya.online_store.rest.validators.ItemValidator;
import ru.devtrifanya.online_store.rest.utils.MainClassConverter;
import ru.devtrifanya.online_store.rest.dto.entities_dto.ItemFeatureDTO;
import ru.devtrifanya.online_store.rest.dto.requests.AddOrUpdateItemRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

        // Преобразование из Map<Integer, ItemFeatureDTO> в Map<Integer, ItemFeature>
        Map<Integer, ItemFeature> itemFeatures = new HashMap<>();
        for (Map.Entry<Integer, ItemFeatureDTO> itemFeature : request.getItemFeatures().entrySet()) {
            itemFeatures.put(
                    itemFeature.getKey(),
                    converter.convertToItemFeature(itemFeature.getValue())
            );
        }
        // Сохранение товара
        itemService.createNewItem(
                converter.convertToItem(request.getItem()),
                request.getCategoryId(),
                itemFeatures,
                request.getItemImages().stream()
                        .map(converter::convertToImage)
                        .collect(Collectors.toList())
        );

        return ResponseEntity.ok("Товар успешно добавлен.");
    }

    /**
     * Адрес: .../items/updateItem
     * Обновление информации о товаре, только для администратора.
     */
    @PatchMapping("/updateItem")
    public ResponseEntity<?> updateItem(@RequestBody @Valid AddOrUpdateItemRequest request) {
        itemValidator.performUpdatedItemValidation(request);

        // Преобразование из Map<Integer, ItemFeatureDTO> в Map<Integer, ItemFeature>
        Map<Integer, ItemFeature> itemFeatures = new HashMap<>();
        for (Map.Entry<Integer, ItemFeatureDTO> itemFeature : request.getItemFeatures().entrySet()) {
            itemFeatures.put(
                    itemFeature.getKey(),
                    converter.convertToItemFeature(itemFeature.getValue())
            );
        }
        // Обновление товара
        itemService.updateItem(
                converter.convertToItem(request.getItem()),
                request.getCategoryId(),
                itemFeatures,
                request.getItemImages().stream()
                        .map(converter::convertToImage)
                        .collect(Collectors.toList())
        );


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

