package ru.devtrifanya.online_store.services;

import org.springframework.mail.MailSender;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;

import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.models.CartElement;
import ru.devtrifanya.online_store.exceptions.NotFoundException;
import ru.devtrifanya.online_store.repositories.CartElementRepository;
import ru.devtrifanya.online_store.rest.validators.CartValidator;

import java.util.List;

@Service
public class CartElementService {
    private final UserService userService;
    private final ItemService itemService;
    private final EmailSenderService emailSenderService;

    private final CartElementRepository cartElementRepository;

    private final CartValidator cartValidator;

    @Autowired
    public CartElementService(@Lazy UserService userService, @Lazy ItemService itemService, EmailSenderService emailSenderService,
                              CartElementRepository cartElementRepository,
                              CartValidator cartValidator) {
        this.userService = userService;
        this.itemService = itemService;
        this.emailSenderService = emailSenderService;
        this.cartElementRepository = cartElementRepository;
        this.cartValidator = cartValidator;
    }

    /**
     * Получение размера корзины по id пользователя.
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
    public CartElement updateCartElement(CartElement updatedElement) {
        CartElement elementToUpdate = getCartElement(updatedElement.getId());
        elementToUpdate.setQuantity(updatedElement.getQuantity());

        return cartElementRepository.save(elementToUpdate);
    }

    /**
     * Уменьшение остатков купленных товаров.
     * Удаление товаров из корзины пользователя.
     * Отправка покупателю email с информацией о заказе.
     */
    public void placeAnOrder(User user) {
        List<CartElement> cartElements = cartElementRepository.findAllByUserId(user.getId());

        cartValidator.performOrderValidation(cartElements);

        emailSenderService.sendEmailWithOrderInfo(generateOrderInfo(cartElements), user.getEmail());

        cartElements.stream()
                .forEach(cartElement -> {
                    itemService.reduceItemQuantity(cartElement.getItem(), cartElement.getQuantity());
                    deleteCartElement(cartElement.getId());
                });
    }

    public String generateOrderInfo(List<CartElement> cartElements) {
        StringBuilder orderInfo = new StringBuilder();
        orderInfo
                .append("Ваш заказ успешно оформлен!\n")
                .append("Состав заказа:\n");
        cartElements.stream()
                .forEach(cartElement ->
                        orderInfo
                                .append(" - Товар: " + cartElement.getItem().getName() + "\n")
                                .append("Производитель: " + cartElement.getItem().getManufacturer() + "\n")
                                .append("Стоимость: " + cartElement.getItem().getPrice() + "\n")
                                .append("Количество: " + cartElement.getQuantity() + "\n")
                                .append("Описание: " + cartElement.getItem().getDescription() + "\n")
                );
        double orderPrice = 0;
        for (CartElement element : cartElements) {
            orderPrice += element.getItem().getPrice() * element.getQuantity();
        }
        orderInfo.append("\nИтоговая стоимость заказа: " + orderPrice);

        return orderInfo.toString();
    }

    /**
     * Удаление элемента корзины.
     */
    public void deleteCartElement(int cartElementId) {
        cartElementRepository.deleteById(cartElementId);
    }
}
