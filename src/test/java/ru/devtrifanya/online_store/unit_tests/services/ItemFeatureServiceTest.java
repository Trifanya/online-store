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

    private static final int ITEM_ID = 1;
    private static final int FEATURE_ID = 1;
    private static final int ITEM_FEATURE_ID = 1;

    private static final double NUMERIC_VALUE = 15.0;
    private static final double EMPTY_NUMERIC_VALUE = -1.0;

    private static final String UNITS = "inches";
    private static final String STRING_VALUE_WITH_STRING = "Android";
    private static final String STRING_VALUE_WITH_NUMBER = "15.0";
    private static final String FULL_STRING_VALUE_WITH_UNITS = STRING_VALUE_WITH_NUMBER + " " + UNITS;

    @Mock
    private ItemService itemServiceMock;
    @Mock
    private FeatureService featureServiceMock;
    @Mock
    private ItemFeatureRepository itemFeatureRepoMock;

    @InjectMocks
    private ItemFeatureService testingService;


    @Test
    public void getItemFeaturesByItemId_shouldInvokeRepoAndReturnFeatures() {
        // Given
        List<ItemFeature> expectedItemFeatures = getItemFeatures();
        mockFindAllByItemId();

        // When
        List<ItemFeature> resultFeatures = testingService.getItemFeaturesByItemId(ITEM_ID);

        // Then
        Mockito.verify(itemFeatureRepoMock).findAllByItemId(ITEM_ID);
        Assertions.assertIterableEquals(expectedItemFeatures, resultFeatures);
    }

    @Test
    public void createOrUpdateItemFeature_create_shouldAssignId() {
        // Given
        ItemFeature itemFeatureToSave = getItemFeatureWithoutId(STRING_VALUE_WITH_STRING);
        mockGetItem();
        mockGetFeature(null);
        mockSaveNew();

        // When
        ItemFeature resultFeature = testingService.createOrUpdateItemFeature(itemFeatureToSave, ITEM_ID, FEATURE_ID);

        // Then
        Mockito.verify(itemFeatureRepoMock).save(itemFeatureToSave);
        Assertions.assertNotNull(resultFeature.getId());
    }

    @Test
    public void createOrUpdateItemFeature_update_shouldAssignFields() {
        // Given
        ItemFeature updatedItemFeature = getItemFeatureWithId(ITEM_FEATURE_ID, STRING_VALUE_WITH_STRING);
        mockGetItem();
        mockGetFeature(null);
        mockSaveUpdated();

        // When
        ItemFeature resultFeature = testingService.createOrUpdateItemFeature(updatedItemFeature, ITEM_ID, FEATURE_ID);

        // Then
        Mockito.verify(itemServiceMock).getItem(ITEM_ID);
        Mockito.verify(featureServiceMock).getFeature(FEATURE_ID);
        Mockito.verify(itemFeatureRepoMock).save(updatedItemFeature);
        Assertions.assertEquals(getItem(), resultFeature.getItem());
        Assertions.assertEquals(getFeature(null), resultFeature.getFeature());
    }

    @Test
    public void createOrUpdateItemFeature_unitIsNotNull_shouldCorrectlyAssignStringAndNumericValue() {
        // Given
        ItemFeature iFeatureWithNumValue = getItemFeatureWithId(ITEM_FEATURE_ID, STRING_VALUE_WITH_NUMBER);
        mockGetItem();
        mockGetFeature(UNITS);
        mockSaveNew();

        // When
        ItemFeature resultItemFeature = testingService.createOrUpdateItemFeature(iFeatureWithNumValue, ITEM_ID, FEATURE_ID);

        // Then
        Assertions.assertEquals(FULL_STRING_VALUE_WITH_UNITS, resultItemFeature.getStringValue());
        Assertions.assertEquals(NUMERIC_VALUE, resultItemFeature.getNumericValue());
    }

    @Test
    public void createOrUpdateItemFeature_unitIsNull_shouldCorrectlyAssignStringAndNumericValue() {
        // Given
        ItemFeature itemFeatureToSave = getItemFeatureWithoutId(STRING_VALUE_WITH_STRING);
        mockGetItem();
        mockGetFeature(null);
        mockSaveNew();

        // When
        ItemFeature resultItemFeature = testingService.createOrUpdateItemFeature(
                itemFeatureToSave, ITEM_ID, FEATURE_ID
        );

        // Then
        Assertions.assertEquals(STRING_VALUE_WITH_STRING, resultItemFeature.getStringValue());
        Assertions.assertEquals(EMPTY_NUMERIC_VALUE, resultItemFeature.getNumericValue());
    }


    // Определение поведения mock-объектов.

    private void mockFindAllByItemId() {
        Mockito.when(itemFeatureRepoMock.findAllByItemId(anyInt()))
                .thenReturn(getItemFeatures());
    }

    private void mockSaveNew() {
        Mockito.doAnswer(
                invocationOnMock -> {
                    ItemFeature itemFeature = invocationOnMock.getArgument(0);
                    itemFeature.setId(ITEM_FEATURE_ID);
                    return itemFeature;
                }).when(itemFeatureRepoMock).save(any(ItemFeature.class));
    }

    public void mockSaveUpdated() {
        Mockito.doAnswer(invocationOnMock -> invocationOnMock.getArgument(0))
                .when(itemFeatureRepoMock).save(any(ItemFeature.class));
    }

    private void mockGetItem() {
        Mockito.when(itemServiceMock.getItem(ITEM_ID))
                .thenReturn(getItem());
    }

    private void mockGetFeature(String units) {
        Mockito.when(featureServiceMock.getFeature(FEATURE_ID))
                .thenReturn(getFeature(units));
    }


    // Вспомогательные методы.

    private ItemFeature getItemFeatureWithId(int id, String stringValue) {
        return new ItemFeature()
                .setId(id)
                .setStringValue(stringValue);
    }

    private ItemFeature getItemFeatureWithoutId(String stringValue) {
        return new ItemFeature()
                .setStringValue(stringValue);
    }

    private Item getItem() {
        return new Item()
                .setId(ITEM_ID);
    }

    private Feature getFeature(String unit) {
        return unit == null ?
                new Feature().setId(FEATURE_ID).setUnit(null)
                :
                new Feature().setId(FEATURE_ID).setUnit(unit);
    }

    private List<ItemFeature> getItemFeatures() {
        return List.of(
                new ItemFeature().setId(1),
                new ItemFeature().setId(2),
                new ItemFeature().setId(3)
        );
    }
}
