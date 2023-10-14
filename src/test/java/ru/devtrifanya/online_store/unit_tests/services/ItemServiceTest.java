package ru.devtrifanya.online_store.unit_tests.services;

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
import ru.devtrifanya.online_store.services.implementations.FeatureService;
import ru.devtrifanya.online_store.services.implementations.ItemService;
import ru.devtrifanya.online_store.exceptions.NotFoundException;

import java.util.*;

import static org.mockito.ArgumentMatchers.eq;

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

    private final int ITEM_ID_1 = 1;
    private final int CATEGORY_ID_1 = 1;
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
    public void getItem_itemIsExist_shouldReturnItem() {
        Item expectedItem = new Item();
        expectedItem.setId(ITEM_ID_1);

        Mockito.when(itemRepository.findById(ITEM_ID_1))
                .thenReturn(Optional.of(expectedItem));
        Item resultItem = itemService.getItem(ITEM_ID_1);

        /** У itemRepository должен вызываться метод findById. */
        Mockito.verify(itemRepository).findById(ITEM_ID_1);
        Assertions.assertNotNull(resultItem);
        Assertions.assertEquals(expectedItem, resultItem);

    }

    @Test
    public void getItem_itemIsNotExist_shouldThrowNotFoundException() {
        Mockito.when(itemRepository.findById(ITEM_ID_1)).thenReturn(Optional.empty());

        Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.getItem(ITEM_ID_1)
        );
        /** У itemRepository должен вызываться метод findById. */
        Mockito.verify(itemRepository).findById(ITEM_ID_1);
    }

    @Test
    public void getItemsByCategory_shouldReturnListOfItems() {
        List<Item> expectedListOfItems = LIST_OF_ITEMS_5;
        Page<Item> itemPage = new PageImpl<>(expectedListOfItems);

        Mockito.when(itemRepository.findAllByCategoryId(
                        CATEGORY_ID_1, PageRequest.of(
                                PAGE_NUMBER_0,
                                ITEMS_PER_PAGE_10,
                                Sort.by(SORT_CRITERION_ID)
                        )))
                .thenReturn(itemPage);
        List<Item> resultListOfItems = itemService.getItemsByCategory(CATEGORY_ID_1, PAGE_NUMBER_0, ITEMS_PER_PAGE_10, SORT_CRITERION_ID);

        /** У itemRepository должен вызываться метод findAllByCategoryId. */
        Mockito.verify(itemRepository).findAllByCategoryId(
                CATEGORY_ID_1, PageRequest.of(
                        PAGE_NUMBER_0,
                        ITEMS_PER_PAGE_10,
                        Sort.by(SORT_CRITERION_ID)
                ));
        Assertions.assertEquals(expectedListOfItems, resultListOfItems);
    }

    @Test
    public void getItemsByCategory_shouldReturnListOfItemsAsPage() {
        List<Item> items = LIST_OF_ITEMS_5;
        Page<Item> itemPage = new PageImpl<>(items.subList(3, 5));
        List<Item> expectedListOfItems = itemPage.getContent();

        Mockito.when(itemRepository.findAllByCategoryId(
                        CATEGORY_ID_1, PageRequest.of(
                                PAGE_NUMBER_1,
                                ITEMS_PER_PAGE_3,
                                Sort.by(SORT_CRITERION_ID)
                        )))
                .thenReturn(itemPage);
        List<Item> resultListOfItems = itemService.getItemsByCategory(CATEGORY_ID_1, PAGE_NUMBER_1, ITEMS_PER_PAGE_3, SORT_CRITERION_ID);

        /** У itemRepository должен вызваться метод findAllByCategoryId. */
        Mockito.verify(itemRepository).findAllByCategoryId(
                CATEGORY_ID_1, PageRequest.of(
                        PAGE_NUMBER_1,
                        ITEMS_PER_PAGE_3,
                        Sort.by(SORT_CRITERION_ID)
                ));
        Assertions.assertEquals(expectedListOfItems, resultListOfItems);
        /** Проверка на совпадение содержимого полученного списка товаров с содержимым ожидаемого списка товаров. */
        for (int i = 0; i < expectedListOfItems.size(); i++) {
            Assertions.assertEquals(
                    expectedListOfItems.get(i),
                    resultListOfItems.get(i)
            );
        }
    }

    @Test
    public void getAll_shouldReturnItemsListSortedBySpecifiedCriterion() {
        List<Item> expectedListOfItems = new ArrayList<>(LIST_OF_ITEMS_5);
        for (int i = 0; i < expectedListOfItems.size(); i++) {
            expectedListOfItems.get(i).setQuantity(i);
        }

        Page<Item> sortedItemPage = new PageImpl<>(expectedListOfItems);

        Mockito.when(itemRepository.findAllByCategoryId(
                        CATEGORY_ID_1, PageRequest.of(
                                PAGE_NUMBER_0,
                                ITEMS_PER_PAGE_10,
                                Sort.by(SORT_CRITERION_QUANTITY)
                        )))
                .thenReturn(sortedItemPage);

        List<Item> resultListOfItems = itemService.getItemsByCategory(CATEGORY_ID_1, PAGE_NUMBER_0, ITEMS_PER_PAGE_10, SORT_CRITERION_QUANTITY);
        resultListOfItems.get(2).setQuantity(-33);

        /** У репозитория itemRepository должен вызываться метод findAllByCategoryId. */
        Mockito.verify(itemRepository).findAllByCategoryId(
                CATEGORY_ID_1, PageRequest.of(
                        PAGE_NUMBER_0,
                        ITEMS_PER_PAGE_10,
                        Sort.by(SORT_CRITERION_QUANTITY)
                ));
        /** Товары из возвращенного списка должны быть отсортированы по количеству, как в исходном списке. */
        /*for (int i = 0; i < resultListOfItems.size(); i++) {
            Assertions.assertEquals(
                    expectedListOfItems.get(i).getQuantity(),
                    resultListOfItems.get(i).getQuantity()
            );
        }*/
        Assertions.assertIterableEquals(expectedListOfItems, resultListOfItems);
    }


    @Test
    public void createNewItem_categoryIsExist_shouldSetItemIdAndCategoryAndInvokeOtherMethods() {
        Item itemToSave = new Item();
        int expectedItemToSaveId = ITEM_ID_1;
        Category itemToSaveCategory = new Category();

        Mockito.when(categoryRepository.findById(CATEGORY_ID_1))
                .thenReturn(Optional.of(itemToSaveCategory));
        Mockito.doAnswer(invocationOnMock -> {
                    Item savedItem = invocationOnMock.getArgument(0);
                    savedItem.setId(expectedItemToSaveId);
                    return savedItem;
                })
                .when(itemRepository).save(itemToSave);

        Item resultItem = itemService.createNewItem(itemToSave, CATEGORY_ID_1);

        /** У репозитоиря categoryRepository должен вызываться метод findById. */
        Mockito.verify(categoryRepository).findById(CATEGORY_ID_1);
        /** У репозитория itemRepository должен вызваться метод save. */
        Mockito.verify(itemRepository).save(itemToSave);
        /** Товару должен присвоиться id, сгенерированный автоматически при обращении к репозиторию. */
        Assertions.assertEquals(expectedItemToSaveId, resultItem.getId());
        /** Товару должна присвоиться категория, id которой был передан в тестируемый метод. */
        Assertions.assertEquals(itemToSaveCategory, resultItem.getCategory());
    }

    @Test
    public void createNewItem_categoryIsExist_shouldNotChangeItemFeaturesListRefAndInvokeOtherMethods() {
        Item itemToSave = new Item();
        Category itemToSaveCategory = new Category();
        List<ItemFeature> itemToSaveFeatures = LIST_OF_ITEM_FEATURES_3;
        itemToSave.setFeatures(itemToSaveFeatures);

        Mockito.when(categoryRepository.findById(CATEGORY_ID_1))
                .thenReturn(Optional.of(itemToSaveCategory));
        Mockito.when(featureRepository.findAllByCategoryId(CATEGORY_ID_1))
                .thenReturn(LIST_OF_FEATURES_3);

        Mockito.doAnswer(invocationOnMock -> {
                    ItemFeature itemFeature = invocationOnMock.getArgument(0);
                    itemFeature.setItem(itemToSave);
                    itemFeature.setFeature(Mockito.any());
                    return itemFeature;
                })
                .when(featureService).createNewItemFeature(
                        Mockito.any(ItemFeature.class),
                        itemToSave,
                        Mockito.any(Feature.class)
                );

        Item resultItem = itemService.createNewItem(itemToSave, CATEGORY_ID_1);

        /** У репозитория featureRepository должен вызываться метод findAllByCategoryId. */
        Mockito.verify(featureRepository).findAllByCategoryId(CATEGORY_ID_1);
        /** У сервиса featureService должен вызваться метод createNewItemFeature для каждой характеристики товара. */

        Mockito.verify(featureService, Mockito.times(LIST_OF_ITEM_FEATURES_3.size())).createNewItemFeature(
                Mockito.any(ItemFeature.class),
                itemToSave,
                Mockito.any(Feature.class)
        );

        /** У товара ссылка на список характеристик не должна меняться. */
        Assertions.assertEquals(itemToSave.getFeatures(), resultItem.getFeatures());
    }

    @Test
    public void createNewItem_categoryIsNotExist_shouldThrowNotFoundException() {
        Item itemToSave = new Item();

        Mockito.when(categoryRepository.findById(CATEGORY_ID_1))
                .thenReturn(Optional.empty());

        /** Тестируемый метод должен выбрасывать исключение. */
        Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.createNewItem(itemToSave, CATEGORY_ID_1)
        );
    }

    @Test
    public void update_categoryIsExist_shouldSetItemIdAndCategoryAndShouldNotChangeItemRefAndInvokeOtherMethods() {
        int itemToUpdateId = ITEM_ID_1;
        Item itemToUpdate = new Item();
        Category itemToUpdateCategory = new Category();

        Mockito.when(categoryRepository.findById(CATEGORY_ID_1))
                .thenReturn(Optional.of(itemToUpdateCategory));

        Item resultItem = itemService.updateItemInfo(ITEM_ID_1, itemToUpdate, CATEGORY_ID_1);

        /** У репозитория categoryRepository должен вызваться метод findById. */
        Mockito.verify(categoryRepository).findById(CATEGORY_ID_1);
        /** У репозитория itemRepository должен вызваться метод save. */
        Mockito.verify(itemRepository).save(itemToUpdate);
        /** Товару должен присвоиться id, переданный в тестируемый метод. */
        Assertions.assertEquals(itemToUpdateId, resultItem.getId());
        /** Товару должна присвоиться категория, id которой был передан в тестируемый метод. */
        Assertions.assertEquals(itemToUpdateCategory, resultItem.getCategory());
        /** Метод должен возвращать тот же товар, который был передан в него. */
        Assertions.assertEquals(itemToUpdate, resultItem);
    }

    @Test
    public void updateItemInfo_categoryIsExist_shouldInvokeMethodsAndNotChangeItemFeaturesListRef() {
        int itemToUpdateId = ITEM_ID_1;
        Item itemToUpdate = new Item();
        Category itemToUpdateCategory = new Category();
        List<ItemFeature> itemToUpdateFeatures = LIST_OF_ITEM_FEATURES_3;
        itemToUpdate.setFeatures(itemToUpdateFeatures);

        Mockito.when(categoryRepository.findById(CATEGORY_ID_1))
                .thenReturn(Optional.of(itemToUpdateCategory));
        Mockito.when(featureRepository.findAllByCategoryId(CATEGORY_ID_1))
                .thenReturn(LIST_OF_FEATURES_3);
        /** Фиктивный featureService при вызове на нем метода createSeveralNewItemFeatures
         * присваивает характеристикам этого товара ненулевые id. */
        Mockito.doAnswer(invocationOnMock -> invocationOnMock.getArgument(0))
                .when(featureService).updateSeveralItemFeaturesInfo(itemToUpdate, LIST_OF_FEATURES_3);

        Item resultItem = itemService.updateItemInfo(itemToUpdateId, itemToUpdate, CATEGORY_ID_1);

        /** У репозитория featureRepository должен вызваться метод findAllByCategoryId. */
        Mockito.verify(featureRepository).findAllByCategoryId(CATEGORY_ID_1);
        /** У сервиса featureService должен вызваться метод updateSeveralItemFeaturesInfo. */
        Mockito.verify(featureService).updateSeveralItemFeaturesInfo(itemToUpdate, LIST_OF_FEATURES_3);
        /** Ссылка на список характеритик у товара не должна меняться. */
        Assertions.assertEquals(itemToUpdateFeatures, resultItem.getFeatures());
    }

    @Test
    public void updateItemInfo_categoryIsNotExist_shouldThrowNotFoundException() {
        Item itemToUpdate = new Item();

        Mockito.when(categoryRepository.findById(CATEGORY_ID_1))
                .thenReturn(Optional.empty());

        /** Тестируемый метод должен выбрасывать исключение. */
        Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.updateItemInfo(ITEM_ID_1, itemToUpdate, CATEGORY_ID_1)
        );
    }

    @Test
    public void deleteItem_shouldInvokeDeleteMethod() {
        itemService.deleteItem(ITEM_ID_1);

        /** У репозитория itemRepository должен вызываться метод deleteById. */
        Mockito.verify(itemRepository).deleteById(ITEM_ID_1);
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
