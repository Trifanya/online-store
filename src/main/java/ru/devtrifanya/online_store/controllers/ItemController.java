package ru.devtrifanya.online_store.controllers;

import jakarta.validation.Valid;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.dto.ItemFeatureDTO;
import ru.devtrifanya.online_store.dto.ItemDTO;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.ItemFeature;
import ru.devtrifanya.online_store.services.ItemService;
import ru.devtrifanya.online_store.util.ErrorResponse;
import ru.devtrifanya.online_store.util.MainExceptionHandler;
import ru.devtrifanya.online_store.util.exceptions.InvalidDataException;
import ru.devtrifanya.online_store.util.validators.ItemValidator;

import java.util.List;

@RestController
@RequestMapping("/categories/{categoryId}")
@Data
public class ItemController {
    private final ItemService itemService;
    private final ModelMapper modelMapper;
    private final ItemValidator itemValidator;
    private final MainExceptionHandler mainExceptionHandler;


    @GetMapping("/{itemId}")
    public ItemDTO show(@PathVariable("itemId") int itemId) {
        // TODO - возможно, вместе с товаром нужно возвращать и его характеристики
        return convertToItemDTO(itemService.get(itemId));
    }

    /**
     * Адрес: .../{categoryId}/new
     * Добавление нового товара, только для администратора.
     * При добавлении нового товара кроме данных о самом товаре, которые хранятся в таблице Item,
     * нужно указать еще и значения характеристик, присущих товару (всем товарам в данной категории).
     * Значения характеристик будут храниться отдельно в таблице ItemCharacteristics.
     */
    @PostMapping("/newItem")
    public ResponseEntity<String> add(@RequestBody @Valid ItemDTO itemDTO,
                                      @RequestBody @Valid List<ItemFeatureDTO> itemFeatures,
                                      @PathVariable("categoryId") int categoryId,
                                      BindingResult bindingResult) {
        itemValidator.validate(itemDTO);

        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            StringBuilder errorMessage = new StringBuilder();
            for (FieldError error : errors) {
                errorMessage.append(error.getDefaultMessage() + "\n");
            }
            throw new InvalidDataException(errorMessage.toString());
        }
        itemService.create(convertToItem(itemDTO), categoryId,
                itemFeatures
                .stream()
                .map(this::convertToItemFeature)
                .toList());
        return ResponseEntity.ok("Товар успешно добавлен.");
    }

    @PatchMapping("/{itemId}/edit")
    public ResponseEntity<String> edit(@RequestBody @Valid ItemDTO itemDTO,
                                       @RequestBody @Valid List<ItemFeatureDTO> itemFeatures,
                                       @PathVariable("itemId") int itemId,
                                       BindingResult bindingResult) {
        itemValidator.validate(itemDTO);

        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            StringBuilder errorMessage = new StringBuilder();
            for (FieldError error : errors) {
                errorMessage.append(error.getDefaultMessage() + "\n");
            }
            throw new InvalidDataException(errorMessage.toString());
        }
        itemService.update(itemId, convertToItem(itemDTO), itemFeatures
                .stream()
                .map(this::convertToItemFeature)
                .toList());
        return ResponseEntity.ok("Информация о товаре успешно изменена.");
    }

    @DeleteMapping("/{itemId}/delete")
    public ResponseEntity<String> delete(@PathVariable("itemId") int itemId) {
        itemService.delete(itemId);
        return ResponseEntity.ok("Товар успешно удален.");
    }

    public Item convertToItem(ItemDTO itemDTO) {
        return modelMapper.map(itemDTO, Item.class);
    }

    public ItemDTO convertToItemDTO(Item item) {
        return modelMapper.map(item, ItemDTO.class);
    }

    public ItemFeature convertToItemFeature(ItemFeatureDTO itemFeatureDTO) {
        return modelMapper.map(itemFeatureDTO, ItemFeature.class);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        return mainExceptionHandler.handleException(exception);
    }
}

