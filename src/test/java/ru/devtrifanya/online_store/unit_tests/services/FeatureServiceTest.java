package ru.devtrifanya.online_store.unit_tests.services;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.models.Feature;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.ItemFeature;
import ru.devtrifanya.online_store.repositories.CategoryRepository;
import ru.devtrifanya.online_store.repositories.FeatureRepository;
import ru.devtrifanya.online_store.repositories.ItemFeatureRepository;
import ru.devtrifanya.online_store.services.FeatureService;
import ru.devtrifanya.online_store.util.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@Data
public class FeatureServiceTest {

    @Mock
    private FeatureRepository featureRepository;
    @Mock
    private ItemFeatureRepository itemFeatureRepository;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private FeatureService featureService;

    private final int FEATURE_ID_1 = 1;
    private final int ITEM_FEATURE_ID_1 = 1;
    private final int CATEGORY_ID_1 = 1;

    private final List<ItemFeature> LIST_OF_ITEM_FEATURES_3 = getItemFeaturesList(3);
    private final List<Feature> LIST_OF_FEATURES_3 = getFeaturesList(3);


    @Test
    public void createNewFeature_categoryIsExist_shouldSetIdAndCategory() {
        Feature featureToSave = new Feature();
        Category featureCategory = new Category();

        Mockito.when(categoryRepository.findById(CATEGORY_ID_1))
                .thenReturn(Optional.of(featureCategory));
        Mockito.doAnswer(invocationOnMock -> {
                    Feature savingFeature = invocationOnMock.getArgument(0);
                    savingFeature.setId(FEATURE_ID_1);
                    return savingFeature;
                })
                .when(featureRepository).save(featureToSave);

        /** Вызов тестируемого метода. */
        Feature resultFeature = featureService.createNewFeature(featureToSave, CATEGORY_ID_1);

        Mockito.verify(categoryRepository).findById(CATEGORY_ID_1);
        Mockito.verify(featureRepository).save(featureToSave);
        Assertions.assertNotNull(resultFeature.getId());
        Assertions.assertEquals(featureCategory, resultFeature.getCategory());
        Assertions.assertEquals(featureToSave, resultFeature);
    }

    @Test
    public void createNewFeature_categoryIsNotExist_shouldThrowNotFoundException() {
        Feature featureToSave = new Feature();

        Mockito.when(categoryRepository.findById(CATEGORY_ID_1))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                NotFoundException.class,
                () -> featureService.createNewFeature(featureToSave, CATEGORY_ID_1)
        );
    }

    @Test
    public void createNewItemFeature_shouldSetIdAndItemAndCategory() {
        ItemFeature itemFeatureToSave = new ItemFeature();
        Item item = new Item();
        Feature feature = new Feature();

        Mockito.doAnswer(invocationOnMock -> {
                    ItemFeature savingItemFeature = invocationOnMock.getArgument(0);
                    savingItemFeature.setId(FEATURE_ID_1);
                    return savingItemFeature;
                })
                .when(itemFeatureRepository).save(itemFeatureToSave);

        /** Вызов тестируемого метода. */
        ItemFeature resultItemFeature = featureService.createNewItemFeature(itemFeatureToSave, item, feature);

        Mockito.verify(itemFeatureRepository).save(itemFeatureToSave);
        Assertions.assertNotNull(resultItemFeature.getId());
        Assertions.assertEquals(item, resultItemFeature.getItem());
        Assertions.assertEquals(feature, resultItemFeature.getFeature());
    }

    @Test
    public void updateFeatureInfo() {

    }


    public List<ItemFeature> getItemFeaturesList(int size) {
        List<ItemFeature> itemFeatures = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            itemFeatures.add(new ItemFeature());
        }
        return itemFeatures;
    }

    public List<Feature> getFeaturesList(int size) {
        List<Feature> featuresList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            featuresList.add(new Feature());
        }
        return featuresList;
    }
}
