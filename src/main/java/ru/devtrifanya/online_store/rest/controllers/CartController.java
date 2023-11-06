package ru.devtrifanya.online_store.rest.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.services.CartElementService;
import ru.devtrifanya.online_store.rest.validators.CartValidator;
import ru.devtrifanya.online_store.rest.utils.MainClassConverter;
import ru.devtrifanya.online_store.rest.dto.requests.AddOrUpdateCartElementRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartElementService cartElementService;

    private final CartValidator cartValidator;

    private final MainClassConverter converter;

    /**
     * Адрес: .../cart/placeAnOrder
     * Оформление заказа, только для пользователя.
     */
    /*@PostMapping("/placeAnOrder")
    public ResponseEntity<?> placeAnOrder(@RequestBody @Valid PlaceAnOrderRequest request) {
        cartValidator.performOrderValidation(request);

        request.getCartContent().stream()
                .forEach(cartElementDTO -> {
                    itemService.reduceItemQuantity(cartElementDTO.getItemId(), cartElementDTO.getQuantity());
                    cartElementService.deleteCartElement(cartElementDTO.getId());
                });

        return ResponseEntity.ok("Заказ успешно оформлен.");
    }*/

    /**
     * Адрес: .../cart/placeAnOrder
     * Оформление заказа, только для пользователя.
     */
    @PostMapping("/placeAnOrder")
    public ResponseEntity<?> placeAnOrder(@AuthenticationPrincipal User user) {
        cartElementService.placeAnOrder(user);

        return ResponseEntity.ok("Заказ успешно оформлен.");
    }

    /**
     * Адрес: .../cart/newCartElement
     * Добавление товара в корзину, только для пользователя.
     */
    @PostMapping("/newCartElement")
    public ResponseEntity<?> createNewCartElement(@RequestBody @Valid AddOrUpdateCartElementRequest request,
                                                  @AuthenticationPrincipal User user) {
        cartValidator.performNewElementValidation(request, user.getId());

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
    @PatchMapping("/updateCartElement")
    public ResponseEntity<?> updateCartElement(@RequestBody @Valid AddOrUpdateCartElementRequest request) {
        cartValidator.performUpdatedElementValidation(request);

        cartElementService.updateCartElement(converter.convertToCartElement(request.getCartElement()));

        return ResponseEntity.ok("Количество товара в корзине успешно изменено.");
    }

    /**
     * Адрес: .../cart/{cartElementId}/deleteCartElement
     * Удаление товара из корзины, только для пользователей.
     */
    @DeleteMapping("/{cartElementId}/deleteCartElement")
    public ResponseEntity<String> deleteCartElement(@PathVariable("cartElementId") int cartElementToDeleteId) {
        cartElementService.deleteCartElement(cartElementToDeleteId);

        return ResponseEntity.ok("Товар успешно удален из корзины.");
    }
}
