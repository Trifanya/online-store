package ru.devtrifanya.online_store.controllers;

import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.dto.CartElementDTO;
import ru.devtrifanya.online_store.models.CartElement;
import ru.devtrifanya.online_store.services.CartElementService;
import ru.devtrifanya.online_store.util.ErrorResponse;
import ru.devtrifanya.online_store.util.MainExceptionHandler;
import ru.devtrifanya.online_store.util.validators.CartValidator;

import java.util.List;

@RestController
@RequestMapping("/{userId}/cart")
@Data
public class CartController {
    private final CartElementService cartElementService;
    private final MainExceptionHandler mainExceptionHandler;
    private final CartValidator cartValidator;
    private final ModelMapper modelMapper;

    @GetMapping
    public List<CartElement> show(@PathVariable("userId") int userId) {
        return cartElementService.getAll(userId);
    }

    @PostMapping("/{itemId}")
    public ResponseEntity<String> add(@RequestBody CartElementDTO dto,
                                      @PathVariable("userId") int userId,
                                      @PathVariable("itemId") int itemId) {
        cartValidator.validate(itemId);
        cartElementService.create(convertToCartElement(dto), userId, itemId);
        return ResponseEntity.ok("Товар успешно добавлен в корзину.");
    }

    @PatchMapping("/{cartElementId}/edit/{itemId}")
    public ResponseEntity<String> edit(@RequestBody CartElementDTO dto,
                                       @PathVariable("userId") int userId,
                                       @PathVariable("itemId") int itemId,
                                       @PathVariable("cartElementId") int cartElementId) {
        cartValidator.validate(itemId, dto.getItemCount());
        cartElementService.update(convertToCartElement(dto), userId, itemId, cartElementId);
        return ResponseEntity.ok("Количество товара в корзине успешно изменено.");
    }

    @DeleteMapping("/delete/{cartElementId}")
    public ResponseEntity<String> delete(@PathVariable("cartElementId") int cartElementId) {
        cartElementService.delete(cartElementId);
        return ResponseEntity.ok("Товар успешно удален из корзины.");
    }


    public CartElement convertToCartElement(CartElementDTO dto) {
        return modelMapper.map(dto, CartElement.class);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        return mainExceptionHandler.handleException(exception);
    }
}
