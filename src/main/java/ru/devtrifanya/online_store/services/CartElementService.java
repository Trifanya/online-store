package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.dto.CartElementDTO;
import ru.devtrifanya.online_store.models.CartElement;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.repositories.CartElementRepository;
import ru.devtrifanya.online_store.repositories.ItemRepository;
import ru.devtrifanya.online_store.repositories.UserRepository;
import ru.devtrifanya.online_store.util.exceptions.NotFoundException;
import ru.devtrifanya.online_store.util.exceptions.OutOfStockException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Data
public class CartElementService {
    private final CartElementRepository cartElementRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    /**
     * Получение корзины (всех элементов корзины) пользователя по id пользователя.
     * и выброс исключения в случае, если корзина пользователя пустая.
     */
    public List<CartElement> getAllCartElements(int userId) {
        List<CartElement> elements = cartElementRepository.findAllByUserId(userId);
        if (elements.isEmpty()) {
            throw new NotFoundException("У Вас в корзине пока что нет ни одного товара.");
        }
        return elements;
    }

    /**
     * Добавление товара в корзину текущего пользователя.
     * При добавлении необходимо проверить, есть ли данный товар в наличии. (проверка в валидаторе)
     * Изначально при добавлении товара в корзину он добавляется в единичном экземпляре.
     */
    public void createCartElement(CartElement element, int userId, int itemId) {
        User user = userRepository.findById(userId).orElse(null);
        Item item = itemRepository.findById(itemId).orElse(null);
        element.setUser(user);
        element.setItem(item);
        cartElementRepository.save(element);
    }

    /**
     * Изменение количества товара в корзине текущего пользователя.
     * При изменении количества необходимо проверить, есть ли данный товар в наличии в
     * указанном количестве. (проверка в валидаторе)
     */
    public void updateCartElement(CartElement element, int userId, int itemId, int cartElementId) {
        User user = userRepository.findById(userId).orElse(null);
        Item item = itemRepository.findById(itemId).orElse(null);
        element.setUser(user);
        element.setItem(item);
        element.setId(cartElementId);
        cartElementRepository.save(element);
    }

    public void deleteCartElement(int cartElementId) {
        cartElementRepository.deleteById(cartElementId);
    }





}
