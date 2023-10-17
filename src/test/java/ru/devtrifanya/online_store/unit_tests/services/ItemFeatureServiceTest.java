package ru.devtrifanya.online_store.unit_tests.services;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.devtrifanya.online_store.models.Feature;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.ItemFeature;
import ru.devtrifanya.online_store.repositories.ItemFeatureRepository;
import ru.devtrifanya.online_store.services.ItemFeatureService;

@ExtendWith(MockitoExtension.class)
@Data
public class ItemFeatureServiceTest {
    @Mock
    private ItemFeatureRepository itemFeatureRepository;

    @InjectMocks
    private ItemFeatureService itemFeatureService;

    /**
     * Данный метод должен выполнять следующие действия:
     * 1. Инициализировать у сохраняемой характеристики поле item параметром метода.
     * 2. Инициализировать у сохраняемой характеристики поле feature параметром метода.
     * 3. Вызывать метод itemFeatureRepository.save()
     * 4. Возвращать тот же объект характеристики, что был возвращен из метода save(),
     *    то есть объект с ненулевым id.
     */
    @Test
    public void createNewItemFeature() {
        int itemFeatureId = 1;
        ItemFeature itemFeatureToSave = new ItemFeature();
        Item expectedItem = new Item();
        Feature expectedFeature = new Feature();

        ItemFeature expectedItemFeature = new ItemFeature();
        expectedItemFeature.setItem(expectedItem);
        expectedItemFeature.setFeature(expectedFeature);

        Mockito.doAnswer(invocationOnMock -> {
                    ItemFeature iFeature = invocationOnMock.getArgument(0);
                    iFeature.setId(itemFeatureId);
                    return iFeature;
                })
                .when(itemFeatureRepository).save(expectedItemFeature);

        ItemFeature resultItemFeature = itemFeatureService.createOrUpdateItemFeature(itemFeatureToSave, expectedItem, expectedFeature);

        Assertions.assertNotNull(resultItemFeature.getId());
        Assertions.assertEquals(expectedItem, resultItemFeature.getItem());
        Assertions.assertEquals(expectedFeature, resultItemFeature.getFeature());

        Mockito.verify(itemFeatureRepository).save(itemFeatureToSave);
    }

    /**
     * Данный метод должен выполнять следующие действия:
     * 1. Инициализировать у сохраняемой характеристики поле item параметром метода.
     * 2. Инициализировать у сохраняемой характеристики поле feature параметром метода.
     * 3. Инициализировать у сохраняемой характеристики поле id параметром метода.
     * 3. Вызывать метод itemFeatureRepository.save()
     * 4. Возвращать тот же объект характеристики, что был возвращен из метода save().
     */
    @Test
    public void updateItemFeatureInfo() {
        int expectedId = 1;
        ItemFeature updatedItemFeature = new ItemFeature();
        Item expectedItem = new Item();
        Feature expectedFeature = new Feature();

        ItemFeature expectedItemFeature = new ItemFeature();
        expectedItemFeature.setItem(expectedItem);
        expectedItemFeature.setFeature(expectedFeature);

        Mockito.when(itemFeatureRepository.save(expectedItemFeature))
                .thenReturn(expectedItemFeature);

        ItemFeature resultItemFeature = itemFeatureService.createOrUpdateItemFeature(
                expectedId, updatedItemFeature, expectedItem, expectedFeature
        );

        Assertions.assertEquals(expectedItem, resultItemFeature.getItem());
        Assertions.assertEquals(expectedFeature, resultItemFeature.getFeature());
        Assertions.assertEquals(expectedId, resultItemFeature.getId());
        Assertions.assertEquals(updatedItemFeature, resultItemFeature);

        Mockito.verify(itemFeatureRepository).save(updatedItemFeature);
    }
}
