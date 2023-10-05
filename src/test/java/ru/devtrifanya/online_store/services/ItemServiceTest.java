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
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.models.Feature;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.ItemFeature;
import ru.devtrifanya.online_store.repositories.CategoryRepository;
import ru.devtrifanya.online_store.repositories.FeatureRepository;
import ru.devtrifanya.online_store.repositories.ItemFeatureRepository;
import ru.devtrifanya.online_store.repositories.ItemRepository;
import ru.devtrifanya.online_store.util.exceptions.NotFoundException;

import java.util.*;

@ExtendWith(MockitoExtension.class)
@Data
public class ItemServiceTest {

    @Mock
    private FeatureService featureService;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private FeatureRepository featureRepository;
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

    private final List<Feature> LIST_OF_FEATURES_3 = getListOfFeatures(3);
    private final List<ItemFeature> LIST_OF_ITEM_FEATURES_3 = getListOfItemFeatures(3);

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
        List<Item> items = LIST_OF_ITEMS_5;
        Page<Item> itemPage = new PageImpl<>(items.subList(3, 5));
        List<Item> expectedResult = itemPage.getContent();

        Mockito.when(itemRepository.findAllByCategoryId(CATEGORY_ID, PageRequest.of(
                PAGE_NUMBER_1,
                ITEMS_PER_PAGE_3,
                Sort.by(SORT_CRITERION_ID)))
        ).thenReturn(itemPage);
        List<Item> realResult = itemService.getItemsByCategory(CATEGORY_ID, PAGE_NUMBER_1, ITEMS_PER_PAGE_3, SORT_CRITERION_ID);

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
    public void createNewItem_categoryIsExist_shouldSetItemIdAndCategoryIdAndInvokeSaveMethod() {
        Item item = new Item();
        int expectedItemId = ITEM_ID;
        Category expectedCategory = new Category();

        Mockito.when(categoryRepository.findById(CATEGORY_ID))
                .thenReturn(Optional.of(expectedCategory));
        Mockito.doAnswer(invocationOnMock -> {
                    Item savedItem = invocationOnMock.getArgument(0);
                    savedItem.setId(expectedItemId);
                    return savedItem;
                })
                .when(itemRepository).save(item);

        Item resultItem = itemService.createNewItem(item, CATEGORY_ID);
        int resultItemId = resultItem.getId();
        Category resultCategory = resultItem.getCategory();

        /** У репозитория itemRepository должен вызваться метод save с аргументом item. */
        Mockito.verify(itemRepository).save(item);
        /** Товару должен присвоиться id, сгенерированный автоматически при обращении к репозиторию. */
        Assertions.assertEquals(expectedItemId, resultItemId);
        /** Товару должна присвоиться категория, id которой был передан в тестируемый метод. */
        Assertions.assertEquals(expectedCategory, resultCategory);
    }

    @Test
    public void createNewItem_categoryIsExist_shouldInvokeFeatureServiceAndSetItemFeaturesId() {
        Item item = new Item();
        Category category = new Category();

        List<ItemFeature> expectedItemFeatures = new ArrayList<>();
        for (int i = 0; i < LIST_OF_ITEM_FEATURES_3.size(); i++) {
            expectedItemFeatures.add(LIST_OF_ITEM_FEATURES_3.get(i));
            expectedItemFeatures.get(i).setId(i + 1);
        }

        Mockito.when(categoryRepository.findById(CATEGORY_ID))
                .thenReturn(Optional.of(category));
        Mockito.when(featureRepository.findAllByCategoryId(CATEGORY_ID))
                        .thenReturn(LIST_OF_FEATURES_3);
        /** Фиктивный featureService при вызове на нем метода createSeveralNewItemFeatures
         * присваивает характеристикам этого товара ненулевые id. */
        Mockito.doAnswer(invocationOnMock -> {
                    Item itemToSave = invocationOnMock.getArgument(0);
                    itemToSave.setFeatures(expectedItemFeatures);
                    return itemToSave;
                })
                .when(featureService).createSeveralNewItemFeatures(item, LIST_OF_FEATURES_3);

        Item resultItem = itemService.createNewItem(item, CATEGORY_ID);
        List<ItemFeature> resultItemFeatures = resultItem.getFeatures();

        /** У сервиса featureService должен вызваться метод createSeveralNewItemFeatures с
         * указанными аргументами. */
        Mockito.verify(featureService).createSeveralNewItemFeatures(item, LIST_OF_FEATURES_3);
        /** У характеристик товара должны быть ненулевые id. */
        Assertions.assertEquals(expectedItemFeatures, resultItemFeatures);

    }

    @Test
    public void createNewItem_categoryIsNotExist_shouldThrowNotFoundException() {
        Item item = new Item();

        Mockito.when(categoryRepository.findById(CATEGORY_ID))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.createNewItem(item, CATEGORY_ID)
        );
    }

    @Test
    public void update_categoryIsExist_shouldInvokeSaveMethod() {
        Item item = new Item();
        Category expectedCategory = new Category();

        Mockito.when(categoryRepository.findById(CATEGORY_ID))
                .thenReturn(Optional.of(expectedCategory));

        Item resultItem = itemService.updateItemInfo(ITEM_ID, item, CATEGORY_ID);
        int resultItemId = resultItem.getId();
        Category resultCategory = resultItem.getCategory();

        /** У репозитория itemRepository должен вызваться метод save с аргументом item. */
        Mockito.verify(itemRepository).save(item);
        /** Товару должен присвоиться id, сгенерированный автоматически при обращении к репозиторию. */
        Assertions.assertEquals(expectedItemId, resultItemId);
        /** Товару должна присвоиться категория, id которой был передан в тестируемый метод. */
        Assertions.assertEquals(expectedCategory, resultCategory);
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

    public List<Feature> getListOfFeatures(int size) {
        List<Feature> features = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            features.add(new Feature());
        }
        return features;
    }

    public List<ItemFeature> getListOfItemFeatures(int size) {
        List<ItemFeature> itemFeatures = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            itemFeatures.add(new ItemFeature());
        }
        return itemFeatures;
    }
}
