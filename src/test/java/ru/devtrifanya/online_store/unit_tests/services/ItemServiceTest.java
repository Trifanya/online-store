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
import ru.devtrifanya.online_store.services.ItemService;
import ru.devtrifanya.online_store.services.CategoryService;
import ru.devtrifanya.online_store.repositories.ItemRepository;
import ru.devtrifanya.online_store.exceptions.NotFoundException;
import ru.devtrifanya.online_store.services.specifications.ItemSpecificationConstructor;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;

@Data
@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    private static final int ITEM_ID = 1;
    private static final int UPDATED_ITEM_ID = 2;
    private static final int CATEGORY_ID = 1;
    private static final int PAGE_NUMBER = 0;
    private static final int ITEMS_PER_PAGE = 3;
    private static final int NEW_REVIEW_RATING = 5;
    private static final int ITEM_QUANTITY = 5;
    private static final int ITEM_TO_BUY_QUANTITY = 3;

    private static final double DEFAULT_RATING = 0;

    private static final String SORT_BY = "id";
    private static final String SORT_DIR_ASC = "ASC";
    private static final String SORT_DIR_DESC = "DESC";
    private static final String SORT_DIR_NONE = "NONE";

    @Mock
    private CategoryService categoryServiceMock;
    @Mock
    private ItemSpecificationConstructor sConstructorMock;
    @Mock
    private ItemRepository itemRepoMock;

    @InjectMocks
    private ItemService testingService;

    @Test
    public void getItem_itemIsExist_shouldReturnItem() {
        // Given
        mockFindById_exist();

        // When
        Item resultItem = testingService.getItem(ITEM_ID);

        // Then
        Mockito.verify(itemRepoMock).findById(ITEM_ID);
        Assertions.assertEquals(getItem(ITEM_ID, DEFAULT_RATING, CATEGORY_ID), resultItem);
    }

    @Test
    public void getItem_itemIsNotExist() {
        // Given
        mockFindById_notExist();

        // When // Then
        Assertions.assertThrows(NotFoundException.class, () -> testingService.getItem(ITEM_ID));
    }

    @Test
    public void getFilteredItems_shouldPassCorrectFiltersToSpecificationConstructor() {
        // Given
        Map<String, String> expectedFilters = Map.of("someKey", "someValue");
        mockGetCategory();
        mockFindAll();

        // When
        List<Item> resultItems = testingService.getFilteredItems(
                CATEGORY_ID, getFilters(), PAGE_NUMBER, ITEMS_PER_PAGE, SORT_BY, SORT_DIR_ASC
        );

        // Then
        Mockito.verify(categoryServiceMock).getCategory(CATEGORY_ID);
        Mockito.verify(sConstructorMock).createItemSpecification(getCategory(CATEGORY_ID), expectedFilters);
    }

    @Test
    public void getFilteredItems_shouldReturnListOfItems() {
        // Given
        mockGetCategory();
        mockFindAll();

        // When
        List<Item> resultItems = testingService.getFilteredItems(
                CATEGORY_ID, getFilters(), PAGE_NUMBER, ITEMS_PER_PAGE, SORT_BY, SORT_DIR_ASC
        );

        // Then
        Mockito.verify(itemRepoMock).findAll(
                (Specification<Item>) null,
                PageRequest.of(
                        PAGE_NUMBER,
                        ITEMS_PER_PAGE,
                        Sort.by(Sort.Direction.valueOf(SORT_DIR_ASC), SORT_BY)
                ));
        Assertions.assertIterableEquals(getItems(), resultItems);
    }

    @Test
    public void reduceItemQuantity() {
        // Given
        mockFindById_exist();
        mockSaveUpdated();

        // When
        Item resultItem = testingService.reduceItemQuantity(ITEM_ID, ITEM_TO_BUY_QUANTITY);

        // Then
        Mockito.verify(itemRepoMock).findById(ITEM_ID);
        Mockito.verify(itemRepoMock).save(any(Item.class));
        Assertions.assertEquals(ITEM_QUANTITY - ITEM_TO_BUY_QUANTITY, resultItem.getQuantity());
    }

    @Test
    public void createNewItem_shouldAssignIdAndZeroRatingAndCategory() {
        // Given
        Item itemToSave = getItem(ITEM_ID);
        mockGetCategory();
        mockSaveNew();

        // When
        Item resultItem = testingService.createNewItem(itemToSave, CATEGORY_ID);

        // Then
        Mockito.verify(categoryServiceMock).getCategory(CATEGORY_ID);
        Mockito.verify(itemRepoMock).save(itemToSave);
        Assertions.assertNotNull(resultItem.getId());
        Assertions.assertEquals(DEFAULT_RATING, resultItem.getRating());
        Assertions.assertEquals(getCategory(CATEGORY_ID), resultItem.getCategory());
    }

    @Test
    public void updateItem_shouldAssignCategoryAndNotChangeRating() {
        // Given
        Item updatedItem = getItem(UPDATED_ITEM_ID);
        mockFindById_exist();
        mockGetCategory();
        mockSaveUpdated();

        // When
        Item resultItem = testingService.updateItem(updatedItem, CATEGORY_ID);

        // Then
        Mockito.verify(itemRepoMock).findById(UPDATED_ITEM_ID);
        Mockito.verify(categoryServiceMock).getCategory(CATEGORY_ID);
        Mockito.verify(itemRepoMock).save(updatedItem);
        Assertions.assertEquals(DEFAULT_RATING, resultItem.getRating());
        Assertions.assertEquals(getCategory(CATEGORY_ID), resultItem.getCategory());
    }

    @Test
    public void updateItemRating_shouldRecalculateRatingAndSaveUpdatedItem() {
        // Given
        mockFindById_exist();
        mockSaveUpdated();

        // When
        Item resultItem = testingService.updateItemRating(ITEM_ID, NEW_REVIEW_RATING);

        // Then
        Mockito.verify(itemRepoMock).findById(ITEM_ID);
        Mockito.verify(itemRepoMock).save(any(Item.class));
        Assertions.assertEquals(NEW_REVIEW_RATING, resultItem.getRating());
    }

    @Test
    public void deleteItem() {
        // When
        testingService.deleteItem(ITEM_ID);

        // Then
        Mockito.verify(itemRepoMock).deleteById(ITEM_ID);
    }


    // Определение поведения mock-объектов.

    private void mockFindById_exist() {
        Mockito.doAnswer(
                invocationOnMock -> Optional.of(
                        getItem(invocationOnMock.getArgument(0), DEFAULT_RATING, CATEGORY_ID)
                )).when(itemRepoMock).findById(anyInt());
    }

    private void mockFindById_notExist() {
         Mockito.when(itemRepoMock.findById(anyInt()))
                 .thenReturn(Optional.empty());
    }

    private void mockFindAll() {
        Mockito.when(itemRepoMock.findAll(
                (Specification<Item>) null,
                PageRequest.of(
                        PAGE_NUMBER,
                        ITEMS_PER_PAGE,
                        Sort.by(Sort.Direction.valueOf(SORT_DIR_ASC), SORT_BY)
                ))).thenReturn(new PageImpl<>(getItems()));
    }

    private void mockSaveNew() {
        Mockito.doAnswer(
                invocationOnMock -> {
                    Item item = invocationOnMock.getArgument(0);
                    item.setId(ITEM_ID);
                    return item;
                }).when(itemRepoMock).save(any(Item.class));
    }

    private void mockSaveUpdated() {
        Mockito.doAnswer(invocationOnMock -> invocationOnMock.getArgument(0))
                .when(itemRepoMock).save(any(Item.class));
    }

    private void mockGetCategory() {
        Mockito.doAnswer(invocationOnMock -> getCategory(invocationOnMock.getArgument(0)))
                .when(categoryServiceMock).getCategory(CATEGORY_ID);
    }


    // Вспомогательные методы.

    private Item getItem(int itemId) {
        return new Item()
                .setId(itemId)
                .setQuantity(ITEM_QUANTITY);
    }

    private Item getItem(int itemId, double rating, int categoryId) {
        return new Item()
                .setId(itemId)
                .setQuantity(ITEM_QUANTITY)
                .setRating(rating)
                .setCategory(getCategory(categoryId))
                .setReviews(new ArrayList<>());
    }

    private Category getCategory(int categoryId) {
        return new Category()
                .setId(categoryId);
    }

    private List<Item> getItems() {
        return List.of(
                new Item().setId(11),
                new Item().setId(12),
                new Item().setId(13)
        );
    }

    private Map<String, String> getFilters() {
        return new HashMap<>(Map.of(
                "pageNumber", "pageNumberValue",
                "itemsPerPage", "itemsPerPageValue",
                "sortBy", "sortByValue",
                "sortDir", "sortDirValue",
                "someKey", "someValue"
        ));
    }
}

