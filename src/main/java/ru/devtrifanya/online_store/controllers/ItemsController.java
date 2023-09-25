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
import ru.devtrifanya.online_store.services.ItemsService;
import ru.devtrifanya.online_store.util.errorResponses.ItemErrorResponse;
import ru.devtrifanya.online_store.util.exceptions.item.InvalidItemDataException;
import ru.devtrifanya.online_store.util.exceptions.item.ItemNotFoundException;
import ru.devtrifanya.online_store.util.validators.ItemValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/items")
@Data
public class ItemsController {
    public final ItemsService itemsService;
    public final ModelMapper modelMapper;
    public final ItemValidator itemValidator;

    @GetMapping("/{id}")
    public ItemDTO getItem(@PathVariable("id") int id) {
        Optional<Item> item = itemsService.findOne(id);
        if (item.isEmpty()) {
            throw new ItemNotFoundException("Товар с такими данными не найден.");
        }
        return convertToItemDTO(item.get());
    }

    @GetMapping("/{category}")
    public List<ItemDTO> getCategoryItems(@PathVariable("category") String category) {
        return itemsService.findItemsByCategory(category)
                .stream()
                .map(this::convertToItemDTO)
                .toList();
    }

    @GetMapping("/{category}/subcategories")
    public List<String> getSubcategories(@PathVariable("category") String category) {
        // TODO
        return null;
    }

    @PostMapping
    public ResponseEntity<HttpStatus> save(@RequestBody @Valid ItemDTO itemDTO,
                                           BindingResult bindingResult) {
        itemValidator.validate(itemDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            StringBuilder errorMessage = new StringBuilder();
            for (FieldError error : errors) {
                errorMessage
                        .append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }
            throw new InvalidItemDataException(errorMessage.toString());
        }
        itemsService.save(convertToItem(itemDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> edit(@RequestBody @Valid ItemDTO itemDTO,
                     @PathVariable("id") int id,
                     BindingResult bindingResult) {
        itemValidator.validate(itemDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            StringBuilder errorMessage = new StringBuilder();
            for (FieldError error : errors) {
                errorMessage
                        .append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }
            throw new InvalidItemDataException(errorMessage.toString());
        }
        itemsService.update(id, convertToItem(itemDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable("id") int id) {
        itemsService.delete(id);
    }

    public Item convertToItem(ItemDTO itemDTO) {
        return modelMapper.map(itemDTO, Item.class);
    }

    public ItemDTO convertToItemDTO(Item item) {
        return modelMapper.map(item, ItemDTO.class);
    }

    @ExceptionHandler
    public ResponseEntity<ItemErrorResponse> handleException(InvalidItemDataException exception) {
        ItemErrorResponse response = new ItemErrorResponse(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ItemErrorResponse> handleException(ItemNotFoundException exception) {
        ItemErrorResponse response = new ItemErrorResponse(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
