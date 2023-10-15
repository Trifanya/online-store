package ru.devtrifanya.online_store.rest.validators;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.devtrifanya.online_store.exceptions.AlreadyExistException;
import ru.devtrifanya.online_store.exceptions.NotFoundException;
import ru.devtrifanya.online_store.models.CartElement;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.repositories.CartElementRepository;
import ru.devtrifanya.online_store.repositories.ItemRepository;
import ru.devtrifanya.online_store.exceptions.OutOfStockException;
import ru.devtrifanya.online_store.rest.dto.entities_dto.CartElementDTO;

import java.rmi.AlreadyBoundException;
import java.util.List;

@Component
@Data
public class CartValidator {
    private final ItemRepository itemRepository;
    private final CartElementRepository cartElementRepository;

    /**
     * Проверка товара на наличие перед оформлением заказа.
     */
    public void validate(List<CartElementDTO> cartContent) {
        for (CartElementDTO cartElement : cartContent) {
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

    public void validate(CartElementDTO cartElement, int userId) {
        if (cartElementRepository.existsByItemIdAndUserId(cartElement.getItemId(), userId)) {
            throw new AlreadyExistException("Товар уже добавлен в корзину.");
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
