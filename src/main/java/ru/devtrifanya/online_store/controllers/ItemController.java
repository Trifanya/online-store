package ru.devtrifanya.online_store.controllers;

import jakarta.validation.Valid;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.dto.ItemDTO;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.services.ItemService;
import ru.devtrifanya.online_store.util.errorResponses.ErrorResponse;
import ru.devtrifanya.online_store.util.exceptions.item.InvalidItemDataException;
import ru.devtrifanya.online_store.util.exceptions.item.ItemNotFoundException;
import ru.devtrifanya.online_store.util.validators.ItemValidator;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/items")
@Data
public class ItemController {
    public final ItemService itemService;
    public final ModelMapper modelMapper;
    public final ItemValidator itemValidator;

    @GetMapping("/{id}")
    public ItemDTO getItem(@PathVariable("id") int id) throws ItemNotFoundException {
        return convertToItemDTO(itemService.findOne(id));
    }

    @GetMapping("/{categoryId}")
    public List<ItemDTO> showItemsOfCategory(@PathVariable("categoryId") int categoryId) {
        return itemService.getItemsOfCategory(categoryId)
                .stream()
                .map(this::convertToItemDTO)
                .toList();
    }

    @PostMapping
    public ResponseEntity<String> addItem(@RequestBody @Valid ItemDTO itemDTO,
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
        itemService.save(convertToItem(itemDTO));
        return ResponseEntity.ok("Товар успешно добавлен.");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> editItem(@RequestBody @Valid ItemDTO itemDTO,
                                       @PathVariable("id") int id,
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
        itemService.update(id, convertToItem(itemDTO));
        return ResponseEntity.ok("Информация о товаре успешно изменена.");
    }

    @DeleteMapping("/{id}")
    public void removeItem(@PathVariable("id") int id) {
        itemService.delete(id);
    }

    public Item convertToItem(ItemDTO itemDTO) {
        return modelMapper.map(itemDTO, Item.class);
    }

    public ItemDTO convertToItemDTO(Item item) {
        return modelMapper.map(item, ItemDTO.class);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(InvalidItemDataException exception) {
        ErrorResponse response = new ErrorResponse(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(ItemNotFoundException exception) {
        ErrorResponse response = new ErrorResponse(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

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

