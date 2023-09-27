package ru.devtrifanya.online_store.controllers;

import jakarta.validation.Valid;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.dto.ItemFeatureDTO;
import ru.devtrifanya.online_store.dto.ItemDTO;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.ItemFeature;
import ru.devtrifanya.online_store.services.ItemService;
import ru.devtrifanya.online_store.util.errorResponses.ErrorResponse;
import ru.devtrifanya.online_store.util.exceptions.item.InvalidItemDataException;
import ru.devtrifanya.online_store.util.exceptions.item.ItemNotFoundException;
import ru.devtrifanya.online_store.util.validators.ItemValidator;

import java.util.List;

@RestController
@RequestMapping("/{categoryId}")
@Data
public class ItemController {
    public final ItemService itemService;
    public final ModelMapper modelMapper;
    public final ItemValidator itemValidator;

    /*@GetMapping()
    public List<ItemDTO> showItemsOfCategory(@PathVariable("categoryId") int categoryId) {
        return itemService.getItemsOfCategory(categoryId)
                .stream()
                .map(this::convertToItemDTO)
                .toList();
    }*/


    @GetMapping("/{itemId}")
    public ItemDTO show(@PathVariable("itemId") int id) throws ItemNotFoundException {
        // TODO - возможно, вместе с товаром нужно возвращать и его характеристики
        return convertToItemDTO(itemService.findOne(id));
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
                                      BindingResult bindingResult) {
        itemValidator.validate(itemDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            StringBuilder errorMessage = new StringBuilder();
            for (FieldError error : errors) {
                errorMessage.append(error.getDefaultMessage() + "\n");
            }
            throw new InvalidItemDataException(errorMessage.toString());
        }
        itemService.save(convertToItem(itemDTO), itemFeatures
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
        itemValidator.validate(itemDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            StringBuilder errorMessage = new StringBuilder();
            for (FieldError error : errors) {
                errorMessage.append(error.getDefaultMessage() + "\n");
            }
            throw new InvalidItemDataException(errorMessage.toString());
        }
        itemService.update(itemId, convertToItem(itemDTO), itemFeatures
                .stream()
                .map(this::convertToItemFeature)
                .toList());
        return ResponseEntity.ok("Информация о товаре успешно изменена.");
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<String> remove(@PathVariable("itemId") int itemId) {
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
        //String errorMessage = null;
        HttpStatus status = null;

        if (exception instanceof InvalidItemDataException) {
            //errorMessage = exception.getMessage();
            status = HttpStatus.UNAUTHORIZED;
        } else if (exception instanceof ItemNotFoundException) {
            //errorMessage = exception.getMessage();
            status = HttpStatus.NOT_FOUND;
        }
        ErrorResponse response = new ErrorResponse(exception.getMessage());
        return new ResponseEntity<>(response, status);
    }
}

