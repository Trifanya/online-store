package ru.devtrifanya.online_store.unit_tests.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.Feature;
import ru.devtrifanya.online_store.models.ItemFeature;
import ru.devtrifanya.online_store.services.ItemService;
import ru.devtrifanya.online_store.services.FeatureService;
import ru.devtrifanya.online_store.services.ItemFeatureService;
import ru.devtrifanya.online_store.repositories.ItemFeatureRepository;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
public class ItemFeatureServiceTest {
    @Mock
    private ItemService itemService;
    @Mock
    private FeatureService featureService;
    @Mock
    private ItemFeatureRepository itemFeatureRepository;

    @InjectMocks
    private ItemFeatureService itemFeatureService;

    private int itemFeatureId = 1;
    private int savedItemFeatureId = 2;

    private int itemId = 1;

    private int featureWithNotNullUnitId = 1;
    private int featureWithNullUnitId = 2;

    private ItemFeature itemFeatureToSaveWithNumericValue = new ItemFeature(0, "1.0");
    private ItemFeature itemFeatureToSaveWithStringValue = new ItemFeature(0, "sv1");
    private ItemFeature updatedItemFeature = new ItemFeature(itemFeatureId, "sv2");

    private Item foundItem = new Item(itemId, "found", "m1", 1000, 1, "d1", 1);

    private Feature featureWithNotNullUnit = new Feature(featureWithNotNullUnitId, "found", "found", "units");
    private Feature featureWithNullUnit = new Feature(featureWithNullUnitId, "found", "found", null);

    private List<ItemFeature> itemFeatures = List.of(
            new ItemFeature(10, "sv10"),
            new ItemFeature(11, "sv11"),
            new ItemFeature(12, "sv12")
    );

    @Test
    public void getItemFeaturesByItemId_shouldInvokeRepoAndReturnFeatures() {
        // Определение поведения mock-объектов
        Mockito.when(itemFeatureRepository.findAllByItemId(itemId))
                .thenReturn(itemFeatures);

        // Выполнение тестируемого метода
        List<ItemFeature> resultFeatures = itemFeatureService.getItemFeaturesByItemId(itemId);

        // Проверка совпадения ожидаемого результата с реальным
        Mockito.verify(itemFeatureRepository).findAllByItemId(itemId);
        Assertions.assertIterableEquals(itemFeatures, resultFeatures);
    }

    @Test
    public void createOrUpdateItemFeature_create_shouldAssignId() {
        // Определение поведения mock-объектов
        createOrUpdateItemFeature_determineBehaviorOfMocks();
        createOrUpdateItemFeature_determineBehaviorOfSaveMethodWhenSaveNew();

        // Проверка совпадения ожидаемых результатов с реальными
        ItemFeature resultFeature = itemFeatureService.createOrUpdateItemFeature(
                itemFeatureToSaveWithNumericValue, itemId, itemFeatureId
        );
        Mockito.verify(itemFeatureRepository).save(itemFeatureToSaveWithNumericValue);
        Assertions.assertNotNull(resultFeature.getId());
    }

    @Test
    public void createOrUpdateItemFeature_update_shouldSaveUpdatedItemFeature() {
        // Определение поведения mock-объектов
        createOrUpdateItemFeature_determineBehaviorOfMocks();
        createOrUpdateItemFeature_determineBehaviorOfSaveMethodWhenSaveUpdated();

        // Проверка совпадения ожидаемых результатов с реальными
        ItemFeature resultFeature = itemFeatureService.createOrUpdateItemFeature(
                updatedItemFeature, itemId, featureWithNullUnitId
        );
        Mockito.verify(itemFeatureRepository).save(updatedItemFeature);
    }

    @Test
    public void createOrUpdateItemFeature_shouldAssignItemAndFeature() {
        // Определение поведения mock-объектов
        createOrUpdateItemFeature_determineBehaviorOfMocks();
        createOrUpdateItemFeature_determineBehaviorOfSaveMethodWhenSaveNew();

        // Проверка совпадения ожидаемых результатов с реальными
        ItemFeature resultFeature = itemFeatureService.createOrUpdateItemFeature(
                itemFeatureToSaveWithNumericValue, itemId, featureWithNullUnitId
        );
        Mockito.verify(itemService).getItem(itemId);
        Mockito.verify(featureService).getFeature(featureWithNullUnitId);
        Assertions.assertEquals(foundItem, resultFeature.getItem());
        Assertions.assertEquals(featureWithNullUnit, resultFeature.getFeature());
    }

    @Test
    public void createOrUpdateItemFeature_unitIsNotNull_shouldCorrectlyAssignStringAndNumericValue() {
        double expectedNumericValue = Double.parseDouble(itemFeatureToSaveWithNumericValue.getStringValue());
        String expectedStringValue = expectedNumericValue + " " + featureWithNotNullUnit.getUnit();

        // Определение поведения mock-объектов
        createOrUpdateItemFeature_determineBehaviorOfMocks();
        createOrUpdateItemFeature_determineBehaviorOfSaveMethodWhenSaveNew();

        ItemFeature resultItemFeature = itemFeatureService.createOrUpdateItemFeature(
                itemFeatureToSaveWithNumericValue, itemId, featureWithNotNullUnitId
        );
        Assertions.assertEquals(expectedStringValue, resultItemFeature.getStringValue());
        Assertions.assertEquals(expectedNumericValue, resultItemFeature.getNumericValue());
    }

    @Test
    public void createOrUpdateItemFeature_unitIsNull_shouldCorrectlyAssignStringAndNumericValue() {
        double expectedNumericValue = -1.0;
        String expectedStringValue = itemFeatureToSaveWithStringValue.getStringValue();

        // Определение поведения mock-объектов
        createOrUpdateItemFeature_determineBehaviorOfMocks();
        createOrUpdateItemFeature_determineBehaviorOfSaveMethodWhenSaveNew();

        ItemFeature resultItemFeature = itemFeatureService.createOrUpdateItemFeature(
                itemFeatureToSaveWithStringValue, itemId, featureWithNullUnitId
        );
        Assertions.assertEquals(expectedStringValue, resultItemFeature.getStringValue());
        Assertions.assertEquals(expectedNumericValue, resultItemFeature.getNumericValue());
    }


    public void createOrUpdateItemFeature_determineBehaviorOfMocks() {
        Mockito.when(itemService.getItem(itemId))
                .thenReturn(foundItem);
        Mockito.doAnswer(
                invocationOnMock -> {
                    int id = invocationOnMock.getArgument(0);
                    return List.of(featureWithNotNullUnit, featureWithNullUnit)
                            .stream()
                            .filter(feature -> feature.getId() == id)
                            .findFirst().orElse(null);
                }).when(featureService).getFeature(anyInt());
    }

    public void createOrUpdateItemFeature_determineBehaviorOfSaveMethodWhenSaveNew() {
        Mockito.doAnswer(
                invocationOnMock -> {
                    ItemFeature itemFeature = invocationOnMock.getArgument(0);
                    itemFeature.setId(savedItemFeatureId);
                    return itemFeature;
                }).when(itemFeatureRepository).save(any(ItemFeature.class));
    }

    public void createOrUpdateItemFeature_determineBehaviorOfSaveMethodWhenSaveUpdated() {
        Mockito.doAnswer(invocationOnMock -> invocationOnMock.getArgument(0))
                .when(itemFeatureRepository).save(any(ItemFeature.class));
    }
}
