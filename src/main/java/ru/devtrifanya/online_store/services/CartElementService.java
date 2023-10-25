package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.models.CartElement;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.repositories.CartElementRepository;
import ru.devtrifanya.online_store.exceptions.NotFoundException;

import java.util.List;

@Service
@Transactional(readOnly = true)
@Data
public class CartElementService {
    private final UserService userService;
    private final ItemService itemService;

    private final CartElementRepository cartElementRepository;

    @Autowired
    public CartElementService(@Lazy UserService userService, @Lazy ItemService itemService,
                              CartElementRepository cartElementRepository) {
        this.userService = userService;
        this.itemService = itemService;
        this.cartElementRepository = cartElementRepository;
    }

    /**
     * Получение размера корзины пользователя по его id.
     */
    public int getCartSizeByUserId(int userId) {
        return cartElementRepository.countAllByUserId(userId);
    }

    /**
     * Получение элемента корзины по его id.
     */
    public CartElement getCartElement(int cartElementId) {
        return cartElementRepository.findById(cartElementId)
                .orElseThrow(() -> new NotFoundException("Элемент корзины с указанным id не найден."));
    }
    /**
     * Получение корзины (всех элементов корзины) пользователя по id пользователя.
     */
    public List<CartElement> getCartElementsByUserId(int userId) {
        return cartElementRepository.findAllByUserId(userId);
    }

    /**
     * Добавление товара в корзину текущего пользователя.
     */
    @Transactional
    public CartElement createNewCartElement(CartElement elementToSave, int userId, int itemId) {
        User user = userService.getUser(userId);
        Item item = itemService.getItem(itemId);

        elementToSave.setUser(user);
        elementToSave.setItem(item);

        return cartElementRepository.save(elementToSave);
    }

    /**
     * Изменение количества единиц конкретного товара в корзине пользователя.
     */
    @Transactional
    public CartElement updateCartElement(CartElement updatedElement) {
        CartElement elementToUpdate = getCartElement(updatedElement.getId());
        elementToUpdate.setItemQuantity(updatedElement.getItemQuantity());

        return cartElementRepository.save(elementToUpdate);
    }

    /**
     * Удаление элемента корзины.
     */
    @Transactional
    public void deleteCartElement(int cartElementId) {
        cartElementRepository.deleteById(cartElementId);
    }





}
