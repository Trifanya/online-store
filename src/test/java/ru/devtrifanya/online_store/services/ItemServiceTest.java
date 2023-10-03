package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.repositories.CategoryRepository;
import ru.devtrifanya.online_store.repositories.ItemFeatureRepository;
import ru.devtrifanya.online_store.repositories.ItemRepository;
import ru.devtrifanya.online_store.util.exceptions.NotFoundException;

import java.util.*;

@ExtendWith(MockitoExtension.class)
@Data
public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ItemFeatureRepository itemFeatureRepository;

    @InjectMocks
    private ItemService itemService;

    private final int ITEM_ID = 1;
    private final int CATEGORY_ID = 1;
    private final int PAGE_NUMBER_0 = 0;
    private final int PAGE_NUMBER_1 = 1;
    private final int ITEMS_PER_PAGE_3 = 3;
    private final int ITEMS_PER_PAGE_10 = 10;

    private final String SORT_CRITERION_ID = "id";
    private final String SORT_CRITERION_PRICE = "price";
    private final String SORT_CRITERION_QUANTITY = "quantity";

    private final List<Item> LIST_OF_ITEMS_5 = getListOfItems(5);
    private final List<Item> LIST_OF_ITEMS_10 = getListOfItems(10);

    @Test
    public void getItem_itemIsExist_shouldReturnItemById() {
        Item item = new Item();
        item.setId(ITEM_ID);

        Mockito.when(itemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
        Item result = itemService.getItem(ITEM_ID);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(item, result);
        Mockito.verify(itemRepository).findById(ITEM_ID);
    }

    @Test
    public void getItem_itemIsNotExist_shouldThrowNotFoundException() {
        Mockito.when(itemRepository.findById(ITEM_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> itemService.getItem(ITEM_ID));
        Mockito.verify(itemRepository).findById(ITEM_ID);
    }

    @Test
    public void getItemsByCategory_shouldReturnAllItemsByCategory() {
        // given
        List<Item> expectedResult = LIST_OF_ITEMS_5;
        Page<Item> itemPage = new PageImpl<>(expectedResult);

        // when
        Mockito.when(itemRepository.findAllByCategoryId(CATEGORY_ID, PageRequest.of(
                        PAGE_NUMBER_0,
                        ITEMS_PER_PAGE_10,
                        Sort.by(SORT_CRITERION_ID)
                )))
                .thenReturn(itemPage);
        List<Item> realResult = itemService.getItemsByCategory(CATEGORY_ID, PAGE_NUMBER_0, ITEMS_PER_PAGE_10, SORT_CRITERION_ID);

        // then
        Assertions.assertEquals(expectedResult, realResult);
        Mockito.verify(itemRepository).findAllByCategoryId(CATEGORY_ID, PageRequest.of(
                PAGE_NUMBER_0,
                ITEMS_PER_PAGE_10,
                Sort.by(SORT_CRITERION_ID)
        ));
    }

    @Test
    public void getItemsByCategory_shouldReturnCorrectPageWithCorrectItems() {
        // given
        List<Item> items = LIST_OF_ITEMS_5;
        Page<Item> itemPage = new PageImpl<>(items.subList(3, 5));
        List<Item> expectedResult = itemPage.getContent();

        // when
        Mockito.when(itemRepository.findAllByCategoryId(CATEGORY_ID, PageRequest.of(
                        PAGE_NUMBER_1,
                        ITEMS_PER_PAGE_3,
                        Sort.by(SORT_CRITERION_ID)
                )))
                .thenReturn(itemPage);
        List<Item> realResult = itemService.getItemsByCategory(CATEGORY_ID, PAGE_NUMBER_1, ITEMS_PER_PAGE_3, SORT_CRITERION_ID);

        // then
        Assertions.assertEquals(expectedResult, realResult);
        Mockito.verify(itemRepository).findAllByCategoryId(CATEGORY_ID, PageRequest.of(
                PAGE_NUMBER_1,
                ITEMS_PER_PAGE_3,
                Sort.by(SORT_CRITERION_ID)
        ));
    }

    @Test
    public void getAll_shouldReturnItemsSortedBySpecifiedCriterion() {
        // given

        // when

        // then
    }


    @Test
    public void createNewItem() {
        Item item = new Item();
        Mockito.doAnswer(invocationOnMock -> {
                    Item savedItem = invocationOnMock.getArgument(0);
                    savedItem.setId(ITEM_ID);
                    return savedItem;
                })
                .when(itemRepository.save(item));

        //Item resultItem = itemService.createNewItem(item, CATEGORY_ID);
    }

    @Test
    public void update_shouldUpdateItemById() {
        // given

        // when

        // then
    }

    @Test
    public void update_shouldUpdateItemFeaturesByItemId() {
        // given

        // when

        // then
    }

    @Test
    public void deleteItem_shouldInvokeDeleteById() {
        itemService.deleteItem(ITEM_ID);
        Mockito.verify(itemRepository).deleteById(ITEM_ID);
    }

    public List<Item> getListOfItems(int size) {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            items.add(new Item());
        }
        return items;
    }
}
