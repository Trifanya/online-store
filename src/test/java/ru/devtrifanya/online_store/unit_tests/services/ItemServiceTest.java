package ru.devtrifanya.online_store.unit_tests.services;

import lombok.Data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.models.Review;
import ru.devtrifanya.online_store.services.ItemService;
import ru.devtrifanya.online_store.services.CategoryService;
import ru.devtrifanya.online_store.repositories.ItemRepository;
import ru.devtrifanya.online_store.exceptions.NotFoundException;
import ru.devtrifanya.online_store.services.specifications.ItemSpecificationConstructor;

import java.util.*;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;

@Data
@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @Mock
    private CategoryService categoryService;
    @Mock
    private ItemSpecificationConstructor specificationConstructor;
    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    private int itemId = 1;
    private int updatedItemId = 1;
    private int itemToDeleteId = 1;

    private int categoryId = 1;

    private int pageNumber = 0;
    private int itemsPerPage = 3;
    private int newReviewRating = 5;
    private int itemToBuyQuantity = 3;

    private String sortBy = "id";
    private String sortDir = "ASC";

    private Item item = new Item(itemId, "found", "m1", 100, 1, "d1", 1);
    private Item itemToSave = new Item(0, "new", "m2", 200, 2, "d2", 2);
    private Item updatedItem = new Item(updatedItemId, "updated", "m3", 300, 3, "d3", 3);

    private Category foundCategory = new Category(categoryId, "found");

    private List<Item> items = List.of(
            new Item(10, "item10", "m10", 1000, 10, "d10", 4),
            new Item(11, "item11", "m11", 1100, 11, "d11", 5)
    );

    private Map<String, String> filters = Map.of(
                    "pageNumber", "pageNumberValue",
                    "itemsPerPage", "itemsPerPageValue",
                    "sortBy", "sortByValue",
                    "sortDir", "sortDirValue",
                    "someKey", "someValue"
    );

    @Test
    public void getItem_itemIsExist_shouldReturnItem() {
        // Определение поведения mock-объектов
        Mockito.when(itemRepository.findById(itemId))
                .thenReturn(Optional.of(item));

        // Выполнение тестируемого метода
        Item resultItem = itemService.getItem(itemId);

        // Проверка совпадения ожидаемых результатов с реальными
        Mockito.verify(itemRepository).findById(itemId);
        Assertions.assertEquals(item, resultItem);
    }

    @Test
    public void getItem_itemIsNotExist() {
        // Определение поведения mock-объектов
        Mockito.when(itemRepository.findById(itemId))
                .thenReturn(Optional.empty());

        // Выполнение тестируемого метода и проверка совпадения ожидаемых вызовов методов с реальными
        Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.getItem(itemId)
        );
        Mockito.verify(itemRepository).findById(itemId);
    }

    @Test
    public void getFilteredItems_shouldPassCorrectFiltersToSpecificationConstructor() {
        // Ожидаемые результаты
        Map<String, String> expectedFilters = new HashMap<>();
        expectedFilters.put("someKey", filters.get("someKey"));

        // Определение поведения mock-объектов
        getFilteredItems_determineBehaviourOfMocks();

        // Выполнение тестируемого метода
        List<Item> resultItems = itemService.getFilteredItems(
                categoryId, new HashMap<>(filters), pageNumber, itemsPerPage, sortBy, sortDir
        );

        ArgumentCaptor<Map<String, String>> filtersCaptor = ArgumentCaptor.forClass(HashMap.class);

        // Проверка совпадения ожидаемых результатов с реальными
        Mockito.verify(categoryService).getCategory(categoryId);
        Mockito.verify(specificationConstructor).createItemSpecification(eq(foundCategory), filtersCaptor.capture());
        Assertions.assertIterableEquals(expectedFilters.entrySet(), filtersCaptor.getValue().entrySet());
    }

    @Test
    public void getFilteredItems_shouldReturnListOfItems() {
        Specification<Item> itemSpecification = null;

        // Определение поведения mock-объектов
        getFilteredItems_determineBehaviourOfMocks();

        // Выполнение тестируемого метода
        List<Item> resultItems = itemService.getFilteredItems(
                categoryId, new HashMap<>(filters), pageNumber, itemsPerPage, sortBy, sortDir
        );

        // Проверка совпадения ожидаемых результатов с реальными
        Mockito.verify(itemRepository).findAll(
                itemSpecification,
                PageRequest.of(
                        pageNumber,
                        itemsPerPage,
                        Sort.by(Sort.Direction.valueOf(sortDir), sortBy)
                ));
        Assertions.assertIterableEquals(items, resultItems);
    }

    @Test
    public void reduceItemQuantity() {
        // Ожидаемые результаты
        int expectedQuantity = item.getQuantity() - itemToBuyQuantity;

        // Определения поведения mock-объектов
        Mockito.when(itemRepository.findById(itemId))
                .thenReturn(Optional.of(item));
        Mockito.when(itemRepository.save(item))
                .thenReturn(item);

        // Выполнение тестируемого метода
        Item resultItem = itemService.reduceItemQuantity(itemId, itemToBuyQuantity);

        // Проверка совпадения ожидаемых вызовов методов с реальными
        Mockito.verify(itemRepository).findById(itemId);
        Mockito.verify(itemRepository).save(item);
        // Проверка совпадения ожидаемого результата с реальным
        Assertions.assertEquals(expectedQuantity, resultItem.getQuantity());
    }

    @Test
    public void createNewItem_shouldAssignId() {
        // Определение поведения mock-объектов
        createNewItem_determineBehaviourOfMocks();

        // Выполнение тестируемого метода
        Item resultItem = itemService.createNewItem(itemToSave, categoryId);

        // Проверка совпадения ожидаемых вызовов методов с реальными
        Mockito.verify(categoryService).getCategory(categoryId);
        Mockito.verify(itemRepository).save(itemToSave);
        // Проверка совпадения ожидаемых результатов с реальными
        Assertions.assertNotNull(resultItem.getId());
        Assertions.assertTrue(resultItem.getRating() == 0);
        Assertions.assertEquals(foundCategory, resultItem.getCategory());
    }

    @Test
    public void createNewItem_shouldAssignCategory() {
        // Определение поведения mock-объектов
        createNewItem_determineBehaviourOfMocks();

        // Выполнение тестируемого метода
        Item resultItem = itemService.createNewItem(itemToSave, categoryId);

        // Проверка совпадения ожидаемых результатов с реальными
        Mockito.verify(categoryService).getCategory(categoryId);
        Assertions.assertEquals(foundCategory, resultItem.getCategory());
    }

    @Test
    public void createNewItem_shouldAssignZeroRating() {
        // Определение поведения mock-объектов
        createNewItem_determineBehaviourOfMocks();

        // Выполнение тестируемого метода
        Item resultItem = itemService.createNewItem(itemToSave, categoryId);

        // Проверка совпадения ожидаемых результатов с реальными
        Assertions.assertTrue(resultItem.getRating() == 0);
    }

    @Test
    public void updateItemInfo_shouldNotChangeRating() {
        // Определение поведения mock-объектов
        updateItemInfo_determineBehaviourOfMocks();

        // Выполнение тестируемого метода
        Item resultItem = itemService.updateItemInfo(updatedItem, categoryId);

        // Проверка совпадения ожидаемых результатов с реальными
        Mockito.verify(itemRepository).findById(updatedItemId);
        Assertions.assertEquals(item.getRating(), resultItem.getRating());
    }

    @Test
    public void updateItemInfo_shouldAssignCategory() {
        // Определение поведения mock-объектов
        updateItemInfo_determineBehaviourOfMocks();

        // Выполнение тестируемого метода
        Item resultItem = itemService.updateItemInfo(updatedItem, categoryId);

        // Проверка совпадения ожидаемых результатов с реальными
        Mockito.verify(categoryService).getCategory(categoryId);
        Assertions.assertNotNull(resultItem.getId());
        Assertions.assertEquals(foundCategory, resultItem.getCategory());
    }

    @Test
    public void updateItemInfo_shouldSaveUpdatedItem() {
        // Определение поведения mock-объектов
        updateItemInfo_determineBehaviourOfMocks();

        // Выполнение тестируемого метода
        Item resultItem = itemService.updateItemInfo(updatedItem, categoryId);

        // Проверка совпадения ожидаемых результатов с реальными
        Mockito.verify(itemRepository).save(updatedItem);
    }

    @Test
    public void updateItemRating_shouldRecalculateRatingAndSaveUpdatedItem() {
        Item item = new Item();
        item.setId(itemId);
        item.setRating(1);
        item.setReviews(List.of(new Review(1, 1, "comment")));

        double expectedRating = 3.0;

        // Определение поведения mock-объектов
        Mockito.when(itemRepository.findById(itemId))
                .thenReturn(Optional.of(item));
        Mockito.doAnswer(invocationOnMock -> invocationOnMock.getArgument(0))
                .when(itemRepository).save(any(Item.class));

        // Выполнение тестируемого метода
        Item resultItem = itemService.updateItemRating(itemId, newReviewRating);

        // Проверка совпадения ожидаемых вызовов методов с реальными
        Mockito.verify(itemRepository).findById(itemId);
        Mockito.verify(itemRepository).save(any(Item.class));
        Assertions.assertEquals(expectedRating, resultItem.getRating());
    }

    @Test
    public void deleteItem() {
        // Выполнение тестируемого метода
        itemService.deleteItem(itemToDeleteId);

        // Проверка совпадения ожидаемых вызовов методов с реальными
        Mockito.verify(itemRepository).deleteById(itemToDeleteId);
    }



    public void getFilteredItems_determineBehaviourOfMocks() {
        Specification<Item> itemSpecification = null;

        Mockito.when(categoryService.getCategory(categoryId))
                .thenReturn(foundCategory);

        Mockito.when(specificationConstructor.createItemSpecification(eq(foundCategory), any(HashMap.class)))
                .thenReturn(itemSpecification);

        Mockito.when(itemRepository.findAll(
                itemSpecification,
                PageRequest.of(
                        pageNumber,
                        itemsPerPage,
                        Sort.by(Sort.Direction.valueOf(sortDir), sortBy)
                ))).thenReturn(new PageImpl<>(items));
    }

    public void createNewItem_determineBehaviourOfMocks() {
        Mockito.when(categoryService.getCategory(categoryId))
                .thenReturn(foundCategory);
        Mockito.doAnswer(
                invocationOnMock -> {
                    Item item = invocationOnMock.getArgument(0);
                    item.setId(1);
                    return item;
                }
        ).when(itemRepository).save(itemToSave);
    }

    public void updateItemInfo_determineBehaviourOfMocks() {
        Mockito.when(itemRepository.findById(updatedItemId))
                .thenReturn(Optional.of(item));
        Mockito.when(categoryService.getCategory(categoryId))
                .thenReturn(foundCategory);
        Mockito.when(itemRepository.save(updatedItem))
                .thenReturn(updatedItem);
    }
}

