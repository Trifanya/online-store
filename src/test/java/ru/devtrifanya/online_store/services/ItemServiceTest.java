package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.repositories.CategoryRepository;
import ru.devtrifanya.online_store.repositories.ItemFeatureRepository;
import ru.devtrifanya.online_store.repositories.ItemRepository;

import java.util.Optional;

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

    @Test
    public void get_shouldReturnItemById() {
        int itemId = 1;

        Item item = new Item();
        item.setId(itemId);
        item.setName("iPhone 14 Pro Max 256Gb");
        item.setDescription("Какое-то описание");
        item.setCategory(null);
        item.setPrice(94000);
        item.setImageURL("Какой-то URL");
        item.setQuantity(3);

        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        Item result = itemService.getItem(1);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(item, result);
    }

    @Test
    public void get_shouldReturnItemCharacteristicsByItemId() {
        // given

        // when

        // then
    }

    @Test
    public void getAll_shouldReturnAllItemsByCategory() {
        // given

        // when

        // then
    }

    @Test
    public void getAll_shouldReturnCorrectPageWithCorrectItems() {
        // given

        // when

        // then
    }

    @Test
    public void getAll_shouldReturnItemsSortedBySpecifiedCriterion() {
        // given

        // when

        // then
    }

    @Test
    public void create_shouldSaveItem() {
        // given

        // when

        // then
    }

    @Test
    public void create_shouldSaveItemFeatures() {
        // given

        // when

        // then
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
    public void delete_shouldDeleteItemById() {
        // given

        // when

        // then
    }

    @Test
    public void delete_shouldDeleteItemCharacteristicsByItemId() {
        // given

        // when

        // then
    }

    @Test
    public void delete_shouldDeleteCartElementByItemId() {
        // given

        // when

        // then
    }

    @Test
    public void delete_shouldDeleteReviewByItemId() {
        // given

        // when

        // then
    }
}
