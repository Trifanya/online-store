package ru.devtrifanya.online_store.controllers;

import jakarta.validation.Valid;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.dto.ItemFeatureDTO;
import ru.devtrifanya.online_store.dto.ItemDTO;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.ItemFeature;
import ru.devtrifanya.online_store.services.FeatureService;
import ru.devtrifanya.online_store.services.ItemService;
import ru.devtrifanya.online_store.util.ErrorResponse;
import ru.devtrifanya.online_store.util.MainExceptionHandler;
import ru.devtrifanya.online_store.util.validators.ItemValidator;


@RestController
@RequestMapping("/categories/{categoryId}")
@Data
public class ItemController {
    private final ItemService itemService;
    private final FeatureService featureService;
    private final ModelMapper modelMapper;
    private final ItemValidator itemValidator;
    private final MainExceptionHandler mainExceptionHandler;


    @GetMapping("/{itemId}")
    public ItemDTO showItemInfo(@PathVariable("itemId") int itemId) {
        Item item = itemService.getItem(itemId);

        ItemDTO itemDTO = convertToItemDTO(item);

        return itemDTO;
    }

    /**
     * Адрес: .../{categoryId}/new
     * Добавление нового товара, только для администратора.
     * При добавлении нового товара кроме данных о самом товаре, которые хранятся в таблице Item,
     * нужно указать еще и значения характеристик, присущих товару (всем товарам в данной категории).
     * Значения характеристик будут храниться отдельно в таблице ItemCharacteristics.
     */
    @PostMapping("/newItem")
    public ResponseEntity<String> addNewItem(@RequestBody @Valid ItemDTO itemDTO,
                                             @PathVariable("categoryId") int categoryId,
                                             BindingResult bindingResult) {
        itemValidator.validate(itemDTO);
        if (bindingResult.hasErrors()) {
            mainExceptionHandler.throwInvalidDataException(bindingResult);
        }

        Item item = convertToItem(itemDTO);

        itemService.createNewItem(item, categoryId);
        return ResponseEntity.ok("Товар успешно добавлен.");
    }

    @PatchMapping("/{itemId}/edit")
    public ResponseEntity<String> editItemInfo(@RequestBody @Valid ItemDTO itemDTO,
                                               @PathVariable("itemId") int itemId,
                                               BindingResult bindingResult) {
        itemValidator.validate(itemDTO);
        if (bindingResult.hasErrors()) {
            mainExceptionHandler.throwInvalidDataException(bindingResult);
        }

        Item item = convertToItem(itemDTO);

        itemService.updateItemInfo(itemId, item);
        return ResponseEntity.ok("Информация о товаре успешно изменена.");
    }

    @DeleteMapping("/{itemId}/delete")
    public ResponseEntity<String> deleteItem(@PathVariable("itemId") int itemId) {
        itemService.deleteItem(itemId);
        return ResponseEntity.ok("Товар успешно удален.");
    }

    public Item convertToItem(ItemDTO itemDTO) {
        Item item = modelMapper.map(itemDTO, Item.class);

        item.setFeatures(itemDTO.getFeatures()
                .stream()
                .map(this::convertToItemFeature)
                .toList());

        return item;
    }

    public ItemDTO convertToItemDTO(Item item) {
        ItemDTO itemDTO = modelMapper.map(item, ItemDTO.class);

        itemDTO.setFeatures(item.getFeatures()
                .stream()
                .map(this::convertToItemFeatureDTO)
                .toList());

        return itemDTO;
    }

    public ItemFeature convertToItemFeature(ItemFeatureDTO itemFeatureDTO) {
        return modelMapper.map(itemFeatureDTO, ItemFeature.class);
    }

    public ItemFeatureDTO convertToItemFeatureDTO(ItemFeature itemFeature) {
        return modelMapper.map(itemFeature, ItemFeatureDTO.class);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        return mainExceptionHandler.handleException(exception);
    }
}

