package ru.devtrifanya.online_store.rest.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.rest.dto.entities_dto.CartElementDTO;
import ru.devtrifanya.online_store.services.implementations.CartElementService;
import ru.devtrifanya.online_store.rest.dto.requests.NewCartElementRequest;
import ru.devtrifanya.online_store.rest.utils.MainClassConverter;
import ru.devtrifanya.online_store.rest.validators.CartValidator;
import ru.devtrifanya.online_store.services.implementations.ItemService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartElementService cartElementService;
    private final ItemService itemService;

    private final CartValidator cartValidator;

    private final MainClassConverter converter;

    /**
     * Оформление заказа.
     */
    @PostMapping("/cart/placeAnOrder")
    public ResponseEntity<?> placeAnOrder(@RequestBody List<@Valid CartElementDTO> cartContent,
                                          @AuthenticationPrincipal User user) {
        cartValidator.validate(cartContent);

        for (CartElementDTO cartElement : cartContent) {
            itemService.buyItem(cartElement.getItemId(),cartElement.getItemCount()); // уменьшение количества купленного товара
            cartElementService.deleteCartElement(cartElement.getId()); // удаление купленного товара из корзины
        }
        return ResponseEntity.ok("Заказ успешно оформлен.");
    }

    /**
     * Добавление товара в корзину, только для пользователей.
     */
    @PostMapping("/catalog/{categoryId}/{itemId}/newCartElement")
    public ResponseEntity<?> createNewCartElement(@RequestBody @Valid NewCartElementRequest request,
                                                  @AuthenticationPrincipal User user) {
        cartValidator.validate(request.getCartElement(), user.getId());

        cartElementService.createNewCartElement(
                converter.convertToCartElement(request.getCartElement()),
                user.getId(),
                request.getCartElement().getItemId()
        );
        return ResponseEntity.ok("Товар успешно добавлен в корзину.");
    }

    /**
     * Изменение количества товара в корзине, только для пользователей.
     */
    @PatchMapping("/cart/updateCartElement")
    public ResponseEntity<?> updateCartElement(@RequestBody @Valid CartElementDTO cartElementDTO) {
        cartValidator.validate(cartElementDTO.getId(), cartElementDTO.getItemCount());

        cartElementService.updateCartElement(
                converter.convertToCartElement(cartElementDTO)
        );
        return ResponseEntity.ok("Количество товара в корзине успешно изменено.");
    }

    /**
     * Удаление товара из корзины, только для пользователей.
     */
    @DeleteMapping("/cart/deleteCartElement")
    public ResponseEntity<String> deleteCartElement(@RequestBody int cartElementId) {
        cartElementService.deleteCartElement(cartElementId);
        return ResponseEntity.ok("Товар успешно удален из корзины.");
    }
}
