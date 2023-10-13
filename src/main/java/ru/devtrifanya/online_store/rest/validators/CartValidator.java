package ru.devtrifanya.online_store.rest.validators;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.devtrifanya.online_store.repositories.ItemRepository;
import ru.devtrifanya.online_store.exceptions.OutOfStockException;

@Component
@Data
public class CartValidator {
    private final ItemRepository itemRepository;

    /**
     * Проверка товара на наличие перед оформлением заказа.
     */
    public void validate(int itemId) {
        if (itemRepository.findById(itemId).get().getQuantity() == 0) {
            throw new OutOfStockException("Данного товара нет в наличии.");
        }
    }

    /**
     * Проверка количества товара при увеличении количества данного товара в корзине.
     */
    public void validate(int itemId, int itemQuantity) {
        if (itemRepository.findById(itemId).get().getQuantity() < itemQuantity) {
            throw new OutOfStockException("Недостаточно товара в наличии.");
        }
    }
}
