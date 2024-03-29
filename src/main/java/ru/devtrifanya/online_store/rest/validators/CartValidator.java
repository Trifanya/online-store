package ru.devtrifanya.online_store.rest.validators;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import ru.devtrifanya.online_store.models.CartElement;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.repositories.ItemRepository;
import ru.devtrifanya.online_store.exceptions.NotFoundException;
import ru.devtrifanya.online_store.exceptions.OutOfStockException;
import ru.devtrifanya.online_store.exceptions.AlreadyExistException;
import ru.devtrifanya.online_store.repositories.CartElementRepository;
import ru.devtrifanya.online_store.rest.dto.entities_dto.CartElementDTO;
import ru.devtrifanya.online_store.rest.dto.requests.PlaceAnOrderRequest;
import ru.devtrifanya.online_store.rest.dto.requests.AddOrUpdateCartElementRequest;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CartValidator {
    private final ItemRepository itemRepository;
    private final CartElementRepository cartElementRepository;

    /**
     * Валидация запроса на добавление товара в корзину.
     */
    public void performNewElementValidation(AddOrUpdateCartElementRequest request, int userId) {
        validateAlreadyInCart(request.getCartElement().getItemId(), userId);
    }

    /**
     * Валидация запроса на изменение количества товаров в корзине.
     */
    public void performUpdatedElementValidation(AddOrUpdateCartElementRequest request) {
        validateItemQuantity(request.getCartElement().getItemId(), request.getCartElement().getQuantity());
    }

    /**
     * Валидация запроса на оформление заказа.
     */
    public void performOrderValidation(List<CartElement> cartElements) {
        validateEnoughQuantity(cartElements);
    }

    /**
     * Сравнение количества товара в заказе и в наличии.
     */
    private void validateItemQuantity(int itemId, int quantityInCart) {
        Item item = itemRepository.findById(itemId).get();
        if (item.getQuantity() < quantityInCart) {
            throw new OutOfStockException("Недостаточно товара в наличии.");
        }
    }

    /**
     * Проверка товара на наличие в корзине пользователя.
     */
    private void validateAlreadyInCart(int itemId, int userId) {
        if (cartElementRepository.existsByItemIdAndUserId(itemId, userId)) {
            throw new AlreadyExistException("Товар уже добавлен в корзину.");
        }
    }

    /**
     * Проверка существования элемента корзины с указанным в запросе id.
     */
    private void validateNotExist(int cartElementId) {
        if (cartElementRepository.findById(cartElementId).isEmpty()) {
            throw new NotFoundException("В корзине нет элемента с указанным id.");
        }
    }

    /**
     * Проверка наличия и количества товара при оформлении заказа.
     */
    private void validateEnoughQuantity(List<CartElement> cartContent) {
        for (CartElement cartElement : cartContent) {
            if (cartElement.getItem().getQuantity() == 0) {
                throw new OutOfStockException("Товара нет в наличии.");
            } else if (cartElement.getItem().getQuantity() < cartElement.getQuantity()) {
                throw new OutOfStockException("Недостаточно товара в наличии.");
            }
        }
    }
}
