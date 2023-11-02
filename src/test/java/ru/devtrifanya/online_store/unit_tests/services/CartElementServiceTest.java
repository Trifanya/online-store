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
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
public class CartElementServiceTest {

    private static final int USER_ID = 1;
    private static final int ITEM_ID = 1;
    private static final int CART_ELEMENT_ID = 1;
    private static final int CART_SIZE = 3;
    private static final int ITEM_QUANTITY = 2;

    @Mock
    private UserService userServiceMock;
    @Mock
    private ItemService itemServiceMock;
    @Mock
    private CartElementRepository cartElementRepoMock;

    @InjectMocks
    private CartElementService testingService;

    @Test
    public void getCartSizeByUserId_shouldReturnCorrectSize() {
        // Given
        mockCountAllByUserId();

        // When
        int resultCartSize = testingService.getCartSizeByUserId(USER_ID);

        // Then
        Mockito.verify(cartElementRepoMock).countAllByUserId(USER_ID);
        Assertions.assertEquals(CART_SIZE, resultCartSize);
    }

    @Test
    public void getCartElement_cartElementIsExist_shouldReturnCartElement() {
        // Given
        mockFindById_exist();

        // When
        CartElement resultElement = testingService.getCartElement(CART_ELEMENT_ID);

        // Then
        Mockito.verify(cartElementRepoMock).findById(CART_ELEMENT_ID);
        Assertions.assertEquals(getCartElement(CART_ELEMENT_ID, USER_ID, ITEM_ID), resultElement);
    }

    @Test
    public void getCartElement_cartElementIsNotExist_shouldThrowException() {
        // Given
        mockFindById_notExist();

        // When // Then
        Assertions.assertThrows(NotFoundException.class, () -> testingService.getCartElement(CART_ELEMENT_ID));
    }

    @Test
    public void getCartElementsByUserId() {
        // Given
        mockFindAllByUserId();

        // When
        List<CartElement> resultList = testingService.getCartElementsByUserId(USER_ID);

        // Then
        Mockito.verify(cartElementRepoMock).findAllByUserId(USER_ID);
        Assertions.assertIterableEquals(getCartElements(), resultList);
    }

    @Test
    public void createNewCartElement_shouldAssignIdUserAndItem() {
        // Given
        CartElement cartElementToSave = getCartElement(0);
        mockGetUser();
        mockGetItem();
        mockSaveNew();

        // When
        CartElement resultCartElement = testingService.createNewCartElement(cartElementToSave, USER_ID, ITEM_ID);

        // Then
        Mockito.verify(cartElementRepoMock).save(cartElementToSave);
        Mockito.verify(userServiceMock).getUser(USER_ID);
        Mockito.verify(itemServiceMock).getItem(ITEM_ID);
        Assertions.assertNotNull(resultCartElement.getId());
        Assertions.assertEquals(getUser(USER_ID), resultCartElement.getUser());
        Assertions.assertEquals(getItem(ITEM_ID), resultCartElement.getItem());
    }


    @Test
    public void updateCartElement_shouldAssignNewQuantityOnly() {
        // Given
        CartElement updatedCartElement = getCartElement(CART_ELEMENT_ID);
        mockFindById_exist();
        mockSaveUpdated();

        // When
        CartElement resultElement = testingService.updateCartElement(updatedCartElement);

        // Then
        Mockito.verify(cartElementRepoMock).findById(CART_ELEMENT_ID);
        Mockito.verify(cartElementRepoMock).save(any(CartElement.class));
        Assertions.assertEquals(updatedCartElement.getQuantity(), resultElement.getQuantity());
        Assertions.assertEquals(getUser(USER_ID), resultElement.getUser());
        Assertions.assertEquals(getItem(ITEM_ID), resultElement.getItem());


    }

    @Test
    public void deleteCartElement_shouldInvokeDeleteById() {
        // When
        testingService.deleteCartElement(CART_ELEMENT_ID);

        // Then
        Mockito.verify(cartElementRepoMock).deleteById(CART_ELEMENT_ID);
    }


    // Определение поведения mock-объектов.

    private void mockCountAllByUserId() {
        Mockito.when(cartElementRepoMock.countAllByUserId(USER_ID))
                .thenReturn(CART_SIZE);
    }

    private void mockFindById_exist() {
        Mockito.doAnswer(invocationOnMock -> Optional.of(getCartElement(invocationOnMock.getArgument(0), USER_ID, ITEM_ID)))
                .when(cartElementRepoMock).findById(anyInt());
    }

    private void mockFindById_notExist() {
        Mockito.when(cartElementRepoMock.findById(anyInt()))
                .thenReturn(Optional.empty());
    }

    private void mockFindAllByUserId() {
        Mockito.when(cartElementRepoMock.findAllByUserId(USER_ID))
                .thenReturn(getCartElements());
    }

    private void mockSaveNew() {
        Mockito.doAnswer(
                invocationOnMock -> {
                    CartElement cartElement = invocationOnMock.getArgument(0);
                    cartElement.setId(CART_ELEMENT_ID);
                    return cartElement;
                }).when(cartElementRepoMock).save(any(CartElement.class));
    }

    private void mockSaveUpdated() {
        Mockito.doAnswer(invocationOnMock -> invocationOnMock.getArgument(0))
                .when(cartElementRepoMock).save(any(CartElement.class));
    }

    private void mockGetUser() {
        Mockito.doAnswer(invocationOnMock -> getUser(invocationOnMock.getArgument(0)))
                .when(userServiceMock).getUser(anyInt());
    }

    private void mockGetItem() {
        Mockito.doAnswer(invocationOnMock -> getItem(invocationOnMock.getArgument(0)))
                .when(itemServiceMock).getItem(anyInt());
    }


    // Вспомогательные методы.

    private CartElement getCartElement(int elementId) {
        return new CartElement()
                .setId(elementId)
                .setQuantity(ITEM_QUANTITY);
    }

    private CartElement getCartElement(int elementId, int userId, int itemId) {
        return new CartElement()
                .setId(elementId)
                .setQuantity(ITEM_QUANTITY)
                .setUser(getUser(userId))
                .setItem(getItem(itemId));
    }

    private User getUser(int userId) {
        return new User()
                .setId(userId);
    }

    private Item getItem(int itemId) {
        return new Item()
                .setId(itemId);
    }

    private List<CartElement> getCartElements() {
        return new ArrayList<>(List.of(
                new CartElement().setId(11),
                new CartElement().setId(12),
                new CartElement().setId(13)
        ));
    }
}
