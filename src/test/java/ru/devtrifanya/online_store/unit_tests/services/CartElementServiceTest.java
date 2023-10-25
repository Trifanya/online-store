package ru.devtrifanya.online_store.unit_tests.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.models.CartElement;
import ru.devtrifanya.online_store.services.ItemService;
import ru.devtrifanya.online_store.services.UserService;
import ru.devtrifanya.online_store.services.CartElementService;
import ru.devtrifanya.online_store.exceptions.NotFoundException;
import ru.devtrifanya.online_store.repositories.CartElementRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class CartElementServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private ItemService itemService;
    @Mock
    private CartElementRepository cartElementRepository;

    @InjectMocks
    private CartElementService cartElementService;

    private int userId = 1;
    private int itemId = 1;

    private int cartElementId = 1;
    private int savedCartElementId = 1;

    private int cartSize = 3;

    private User foundUser = new User(userId, "name", "surname", "email@mail.ru", "ROLE_USER");
    private Item foundItem = new Item(itemId, "found", "m1", 100, 1, "d1", 1);

    private CartElement cartElementToSave = new CartElement(0, 2);
    private CartElement updatedCartElement = new CartElement(cartElementId, 3);
    private CartElement foundCartElement = new CartElement(cartElementId, 3, foundUser, foundItem);


    private List<CartElement> cartElements = List.of(
            new CartElement(10, 10),
            new CartElement(11, 11),
            new CartElement(12, 12)
    );

    @Test
    public void getCartSizeByUserId_shouldReturnCorrectSize() {
        // Определение поведения mock-объектов
        Mockito.when(cartElementRepository.countAllByUserId(userId))
                .thenReturn(cartSize);

        // Выполнение тестируемого метода
        int resultCartSize = cartElementService.getCartSizeByUserId(userId);

        // Проверка совпадения ожидаемых результатов с реальными
        Mockito.verify(cartElementRepository).countAllByUserId(userId);
        Assertions.assertEquals(cartSize, resultCartSize);
    }

    @Test
    public void getCartElement_cartElementIsExist_shouldReturnCartElement() {
        // Определение поведения mock-объектов
        Mockito.when(cartElementRepository.findById(cartElementId))
                .thenReturn(Optional.of(foundCartElement));

        // Выполнение тестируемого метода
        CartElement resultElement = cartElementService.getCartElement(cartElementId);

        // Проверка совпадения ожидаемых результатов с реальными
        Mockito.verify(cartElementRepository).findById(cartElementId);
        Assertions.assertEquals(foundCartElement, resultElement);
    }

    @Test
    public void getCartElement_cartElementIsNotExist_shouldThrowException() {
        // Определение поведения mock-объектов
        Mockito.when(cartElementRepository.findById(cartElementId))
                .thenReturn(Optional.empty());

        // Выполнение тестируемого метода и проверка совпадения ожидаемых результатов с реальными
        Assertions.assertThrows(
                NotFoundException.class,
                () -> cartElementService.getCartElement(cartElementId)
        );
        Mockito.verify(cartElementRepository).findById(cartElementId);
    }

    @Test
    public void getCartElementsByUserId() {
        // Определение поведения mock-объектов
        Mockito.when(cartElementRepository.findAllByUserId(userId))
                .thenReturn(cartElements);

        // Выполнение тестируемого метода
        List<CartElement> resultList = cartElementService.getCartElementsByUserId(userId);
        // Проверка совпадения ожидаемого результата с реальным
        Mockito.verify(cartElementRepository).findAllByUserId(userId);
        Assertions.assertIterableEquals(cartElements, resultList);
    }

    @Test
    public void createNewCartElement_shouldAssignId() {
        // Определение поведения mock-объектов
        createNewCartElement_determineBehaviorOfMocks();

        // Выполнение тестируемого метода
        CartElement resultCartElement = cartElementService.createNewCartElement(cartElementToSave, userId, itemId);
        // Проверка совпадения ожидаемого результата с реальным
        Mockito.verify(cartElementRepository).save(cartElementToSave);
        Assertions.assertNotNull(resultCartElement.getId());
    }

    @Test
    public void createNewCartElement_shouldAssignUser() {
        // Определение поведения mock-объектов
        createNewCartElement_determineBehaviorOfMocks();

        // Выполнение тестируемого метода
        CartElement resultCartElement = cartElementService.createNewCartElement(cartElementToSave, userId, itemId);
        // Проверка совпадения ожидаемого результата с реальным
        Mockito.verify(userService).getUser(userId);
        Assertions.assertEquals(foundUser, resultCartElement.getUser());
    }

    @Test
    public void createNewCartElement_shouldAssignItem() {
        // Определение поведения mock-объектов
        createNewCartElement_determineBehaviorOfMocks();

        // Выполнение тестируемого метода
        CartElement resultCartElement = cartElementService.createNewCartElement(cartElementToSave, userId, itemId);
        // Проверка совпадения ожидаемого результата с реальным
        Mockito.verify(itemService).getItem(itemId);
        Assertions.assertEquals(foundItem, resultCartElement.getItem());
    }

    @Test
    public void updateCartElement_shouldAssignNewQuantity() {
        // Определение поведения mock-объектов
        updateCartElement_determineBehaviorOfMocks();

        // Выполнение тестируемого метода
        CartElement resultElement = cartElementService.updateCartElement(updatedCartElement);
        // Проверка совпадения ожидаемого результата с реальным
        Mockito.verify(cartElementRepository).findById(cartElementId);
        Assertions.assertEquals(updatedCartElement.getItemQuantity(), resultElement.getItemQuantity());
    }

    @Test
    public void updateCartElement_shouldNotChangeUser() {
        // Определение поведения mock-объектов
        updateCartElement_determineBehaviorOfMocks();

        // Выполнение тестируемого метода
        CartElement resultElement = cartElementService.updateCartElement(updatedCartElement);
        // Проверка совпадения ожидаемого результата с реальным
        Mockito.verify(cartElementRepository).findById(cartElementId);
        Assertions.assertEquals(foundCartElement.getUser(), resultElement.getUser());
    }

    @Test
    public void updateCartElement_shouldNotChangeItem() {
        // Определение поведения mock-объектов
        updateCartElement_determineBehaviorOfMocks();

        // Выполнение тестируемого метода
        CartElement resultElement = cartElementService.updateCartElement(updatedCartElement);
        // Проверка совпадения ожидаемого результата с реальным
        Mockito.verify(cartElementRepository).findById(cartElementId);
        Assertions.assertEquals(foundCartElement.getItem(), resultElement.getItem());
    }

    @Test
    public void deleteCartElement_shouldInvokeDeleteById() {
        // Выполнение тестируемого метода
        cartElementService.deleteCartElement(cartElementId);
        // Проверка совпадения ожидаемого результата с реальным
        Mockito.verify(cartElementRepository).deleteById(cartElementId);
    }



    public void createNewCartElement_determineBehaviorOfMocks() {
        Mockito.when(userService.getUser(userId))
                .thenReturn(foundUser);
        Mockito.when(itemService.getItem(itemId))
                .thenReturn(foundItem);
        Mockito.doAnswer(
                invocationOnMock -> {
                    CartElement cartElement = invocationOnMock.getArgument(0);
                    cartElement.setId(savedCartElementId);
                    return cartElement;
                }).when(cartElementRepository).save(any(CartElement.class));
    }

    public void updateCartElement_determineBehaviorOfMocks() {
        Mockito.when(cartElementRepository.findById(cartElementId))
                .thenReturn(Optional.of(foundCartElement));
        Mockito.doAnswer(invocationOnMock -> invocationOnMock.getArgument(0))
                .when(cartElementRepository).save(any(CartElement.class));
    }
}
