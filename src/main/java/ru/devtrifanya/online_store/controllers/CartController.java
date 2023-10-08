package ru.devtrifanya.online_store.controllers;

import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.dto.CartElementDTO;
import ru.devtrifanya.online_store.models.CartElement;
import ru.devtrifanya.online_store.services.CartElementService;
import ru.devtrifanya.online_store.util.ErrorResponse;
import ru.devtrifanya.online_store.util.MainClassConverter;
import ru.devtrifanya.online_store.util.MainExceptionHandler;
import ru.devtrifanya.online_store.util.validators.CartValidator;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/{userId}/cart")
@Data
public class CartController {
    private final CartElementService cartElementService;
    private final MainExceptionHandler mainExceptionHandler;
    private final CartValidator cartValidator;
    private final MainClassConverter converter;

    @GetMapping
    public List<CartElementDTO> getCartElements(@PathVariable("userId") int userId) {
        return cartElementService.getCartElementsByUserId(userId)
                .stream()
                .map(cartElement -> converter.convertToCartElementDTO(cartElement))
                .collect(Collectors.toList());
    }

    @PostMapping("/{itemId}")
    public ResponseEntity<String> createNewCartElement(@RequestBody CartElementDTO dto,
                                                @PathVariable("userId") int userId,
                                                @PathVariable("itemId") int itemId) {
        cartValidator.validate(itemId);

        cartElementService.createNewCartElement(
                converter.convertToCartElement(dto),
                userId,
                itemId
        );

        return ResponseEntity.ok("Товар успешно добавлен в корзину.");
    }

    @PatchMapping("/{cartElementId}/edit/{itemId}")
    public ResponseEntity<String> updateCartElement(@RequestBody CartElementDTO dto,
                                               @PathVariable("cartElementId") int cartElementId,
                                               @PathVariable("userId") int userId,
                                               @PathVariable("itemId") int itemId) {
        cartValidator.validate(itemId, dto.getItemCount());

        cartElementService.updateCartElement(
                cartElementId,
                converter.convertToCartElement(dto),
                userId,
                itemId
        );
        return ResponseEntity.ok("Количество товара в корзине успешно изменено.");
    }

    @DeleteMapping("/delete/{cartElementId}")
    public ResponseEntity<String> deleteCartElement(@PathVariable("cartElementId") int cartElementId) {
        cartElementService.deleteCartElement(cartElementId);
        return ResponseEntity.ok("Товар успешно удален из корзины.");
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        return mainExceptionHandler.handleException(exception);
    }
}
