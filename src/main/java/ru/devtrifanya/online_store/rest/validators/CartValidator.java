package ru.devtrifanya.online_store.rest.validators;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.repositories.ItemRepository;
import ru.devtrifanya.online_store.repositories.CartElementRepository;
import ru.devtrifanya.online_store.exceptions.NotFoundException;
import ru.devtrifanya.online_store.exceptions.OutOfStockException;
import ru.devtrifanya.online_store.exceptions.AlreadyExistException;
import ru.devtrifanya.online_store.rest.dto.entities_dto.CartElementDTO;
import ru.devtrifanya.online_store.rest.dto.requests.PlaceAnOrderRequest;
import ru.devtrifanya.online_store.rest.dto.requests.DeleteFromCartRequest;
import ru.devtrifanya.online_store.rest.dto.requests.AddOrUpdateCartElementRequest;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CartValidator {
    private final ItemRepository itemRepository;
    private final CartElementRepository cartElementRepository;

    public void validateNewCartElement(AddOrUpdateCartElementRequest request, int userId) {
        validateAlreadyInCart(request.getCartElement().getItemId(), userId);
    }

    public void validateUpdatedCartElement(AddOrUpdateCartElementRequest request) {
        validateItemQuantity(request.getCartElement().getItemId(), request.getCartElement().getItemCount());
    }

    public void validateOrder(PlaceAnOrderRequest request) {
        validateEnoughQuantity(request.getCartContent());
    }

    public void validateDeleteFromCart(DeleteFromCartRequest request) {
        validateNotExist(request.getCartElementToDeleteId());
    }

    /**
     * Сравнение количества товара в заказе и в наличии.
     */
    public void validateItemQuantity(int itemId, int quantityInCart) {
        Item item = itemRepository.findById(itemId).get();
        if (item.getQuantity() < quantityInCart) {
            throw new OutOfStockException("Недостаточно товара в наличии.");
        }
    }

    /**
     * Проверка товара на наличие в корзине пользователя.
     */
    public void validateAlreadyInCart(int itemId, int userId) {
        if (cartElementRepository.existsByItemIdAndUserId(itemId, userId)) {
            throw new AlreadyExistException("Товар уже добавлен в корзину.");
        }
    }

    /**
     * Проверка существования элемента корзины с указанным в запросе id.
     */
    public void validateNotExist(int cartElementId) {
        if (cartElementRepository.findById(cartElementId).isEmpty()) {
            throw new NotFoundException("В корзине нет элемента с указанным id.");
        }
    }

    /**
     * Проверка наличия и количества товара при оформлении заказа.
     */
    public void validateEnoughQuantity(List<CartElementDTO> cartContent) {
        for (CartElementDTO cartElement : cartContent) {
            Item item = itemRepository.findById(cartElement.getItemId())
                    .orElseThrow(() -> new NotFoundException("Товар с указанным id не найден."));
            if (item.getQuantity() == 0) {
                throw new OutOfStockException("Товара нет в наличии.");
            } else if (item.getQuantity() < cartElement.getItemCount()) {
                throw new OutOfStockException("Недостаточно товара в наличии.");
            }
        }
    }
}
