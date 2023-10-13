package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.models.CartElement;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.repositories.CartElementRepository;
import ru.devtrifanya.online_store.repositories.ItemRepository;
import ru.devtrifanya.online_store.exceptions.NotFoundException;

import java.util.List;

@Service
@Transactional(readOnly = true)
@Data
public class CartElementService {
    private final ItemRepository itemRepository;
    private final CartElementRepository cartElementRepository;

    /**
     * Получение корзины (всех элементов корзины) пользователя по id пользователя.
     * Метод получает на вход id пользователя, вызывает метод репозитория для поиска
     * элементов корзины по этому id, и возвращает найденный список.
     */
    public List<CartElement> getCartElementsByUserId(int userId) {
        return cartElementRepository.findAllByUserId(userId);
    }

    public int getCartSizeByUserId(int userId) {
        return cartElementRepository.countAllByUserId(userId);
    }

    /**
     * Добавление товара в корзину текущего пользователя.
     * Метод получает на вход элемент корзины, у которого проинициализировано только поле
     * count, инициализирует у сохраняемого элемента поля user и item, затем вызывает
     * метод репозитория для сохранения этого элемента в БД и возвращает сохраненный
     * элемент корзины.
     */
    @Transactional
    public CartElement createNewCartElement(CartElement elementToSave, User user, int itemId) {
        Item item = itemRepository.findById(itemId)
                        .orElseThrow(() -> new NotFoundException("Товар с указанным id не найден."));

        elementToSave.setUser(user);
        elementToSave.setItem(item);

        return cartElementRepository.save(elementToSave);
    }

    /**
     * Изменение количества единиц конкретного товара в корзине пользователя.
     * Метод получает на вход id элемента корзины который будет изменен, элемент корзины,
     * у которого проинициализировано только поле count, инициализирует у сохраняемого
     * элемента поля user и item, затем вызывает метод репозитория для сохранения этого
     * элемента в БД и возвращает измененный элемент корзины.
     */
    @Transactional
    public CartElement updateCartElement(CartElement elementToUpdate) {
        CartElement oldCartElement = cartElementRepository.findById(elementToUpdate.getId())
                .orElseThrow(() -> new NotFoundException("Элемент корзины с указанным id не найден."));
        elementToUpdate.setItemCount(oldCartElement.getItemCount());

        return cartElementRepository.save(elementToUpdate);
    }

    /**
     * Удаление элемента корзины.
     * Метод получает на вход id элемента корзины, который нужно удалить, затем вызывает
     * метод репозитория для его удаления.
     */
    @Transactional
    public void deleteCartElement(int cartElementId) {
        cartElementRepository.deleteById(cartElementId);
    }





}
