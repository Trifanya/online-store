package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
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

    /**
     * Получение корзины (всех элементов корзины) пользователя по id пользователя.
     * Метод получает на вход id пользователя, вызывает метод репозитория для поиска
     * элементов корзины по этому id, и возвращает найденный список.
     */
    public List<CartElement> getCartElementsByUserId(int userId) {
        return cartElementRepository.findAllByUserId(userId);
    }

    /**
     * Добавление товара в корзину текущего пользователя.
     * Метод получает на вход элемент корзины, у которого проинициализировано только поле
     * count, инициализирует у сохраняемого элемента поля user и item, затем вызывает
     * метод репозитория для сохранения этого элемента в БД и возвращает сохраненный
     * элемент корзины.
     */
    @Transactional
    public CartElement createNewCartElement(CartElement elementToSave, User user, Item item) {
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
    public CartElement updateCartElement(int cartElementId, CartElement elementToUpdate, User user, Item item) {
        elementToUpdate.setId(cartElementId);
        elementToUpdate.setUser(user);
        elementToUpdate.setItem(item);
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
