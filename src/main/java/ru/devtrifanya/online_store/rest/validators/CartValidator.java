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

@Component
@RequiredArgsConstructor
public class CartValidator {
    private final ItemRepository itemRepository;
    private final CartElementRepository cartElementRepository;

    /**
     * Проверка количества товара при увеличении количества данного товара в корзине.
     */
    public void validate(AddOrUpdateCartElementRequest request) {
        Item item = itemRepository.findById(request.getCartElement().getItemId()).get();
        if (item.getQuantity() < request.getCartElement().getItemCount()) {
            throw new OutOfStockException("Недостаточно товара в наличии.");
        }
    }

    /**
     * Проверка товара на наличие в корзине пользователя.
     */
    public void validate(AddOrUpdateCartElementRequest request, int userId) {
        if (cartElementRepository.existsByItemIdAndUserId(request.getCartElement().getItemId(), userId)) {
            throw new AlreadyExistException("Товар уже добавлен в корзину.");
        }
    }

    /**
     * Проверка товара на наличие перед оформлением заказа.
     */
    public void validate(PlaceAnOrderRequest request) {
        for (CartElementDTO cartElement : request.getCartContent()) {
            Item item = itemRepository.findById(cartElement.getItemId())
                    .orElseThrow(() -> new NotFoundException("Товар с указанным id не найден."));
            if (item.getQuantity() == 0) {
                System.out.println(item.getName() + ": " + item.getQuantity());
                throw new OutOfStockException("Товара нет в наличии.");
            }
            if (item.getQuantity() < cartElement.getItemCount()) {
                System.out.println(item.getName() + ": " + item.getQuantity());
                throw new OutOfStockException("Недостаточно товара в наличии.");
            }
        }
    }

    /**
     * Проверка существования элемента корзины с указанным в запросе id.
     */
    public void validate(DeleteFromCartRequest request) {
        if (cartElementRepository.findById(request.getCartElementToDeleteId()).isEmpty()) {
            throw new NotFoundException("В корзине нет элемента с указанным id.");
        }
    }
}
