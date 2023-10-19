package ru.devtrifanya.online_store.rest.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.rest.dto.entities_dto.CartElementDTO;
import ru.devtrifanya.online_store.rest.dto.requests.DeleteFromCartRequest;
import ru.devtrifanya.online_store.rest.dto.requests.PlaceAnOrderRequest;
import ru.devtrifanya.online_store.services.CartElementService;
import ru.devtrifanya.online_store.rest.dto.requests.AddOrUpdateCartElementRequest;
import ru.devtrifanya.online_store.rest.utils.MainClassConverter;
import ru.devtrifanya.online_store.rest.validators.CartValidator;
import ru.devtrifanya.online_store.services.ItemService;

@RestController
@RequiredArgsConstructor
public class CartController {
    private final ItemService itemService;
    private final CartElementService cartElementService;

    private final CartValidator cartValidator;

    private final MainClassConverter converter;

    /**
     * Адрес: .../cart/placeAnOrder
     * Оформление заказа, только для пользователя.
     */
    @PostMapping("/cart/placeAnOrder")
    public ResponseEntity<?> placeAnOrder(@RequestBody @Valid PlaceAnOrderRequest request) {
        cartValidator.validateOrder(request);

        for (CartElementDTO cartElement : request.getCartContent()) {
            itemService.reduceItemQuantity(cartElement.getItemId(),cartElement.getItemCount()); // уменьшение количества купленного товара
            cartElementService.deleteCartElement(cartElement.getId()); // удаление купленного товара из корзины
        }

        return ResponseEntity.ok("Заказ успешно оформлен.");
    }

    /**
     * Адрес: .../catalog/{categoryId}/itemId}/newCartElement
     * Добавление товара в корзину, только для пользователя.
     */
    @PostMapping("/catalog/{categoryId}/{itemId}/newCartElement")
    public ResponseEntity<?> createNewCartElement(@RequestBody @Valid AddOrUpdateCartElementRequest request,
                                                  @AuthenticationPrincipal User user) {
        cartValidator.validateNewCartElement(request, user.getId());

        cartElementService.createNewCartElement(
                converter.convertToCartElement(request.getCartElement()),
                user.getId(),
                request.getCartElement().getItemId()
        );

        return ResponseEntity.ok("Товар успешно добавлен в корзину.");
    }

    /**
     * Адрес: .../cart/updateCartElement
     * Изменение количества товара в корзине, только для пользователя.
     */
    @PatchMapping("/cart/updateCartElement")
    public ResponseEntity<?> updateCartElement(@RequestBody @Valid AddOrUpdateCartElementRequest request) {
        cartValidator.validateUpdatedCartElement(request);

        cartElementService.updateCartElement(
                converter.convertToCartElement(request.getCartElement())
        );

        return ResponseEntity.ok("Количество товара в корзине успешно изменено.");
    }

    /**
     * Адрес: .../cart/deleteCartElement
     * Удаление товара из корзины, только для пользователей.
     */
    @DeleteMapping("/cart/deleteCartElement")
    public ResponseEntity<String> deleteCartElement(@RequestBody DeleteFromCartRequest request) {
        cartValidator.validateDeleteFromCart(request);

        cartElementService.deleteCartElement(request.getCartElementToDeleteId());

        return ResponseEntity.ok("Товар успешно удален из корзины.");
    }
}
