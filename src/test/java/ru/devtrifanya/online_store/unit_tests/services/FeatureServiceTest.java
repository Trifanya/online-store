package ru.devtrifanya.online_store.unit_tests.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.models.Feature;
import ru.devtrifanya.online_store.services.FeatureService;
import ru.devtrifanya.online_store.services.CategoryService;
import ru.devtrifanya.online_store.exceptions.NotFoundException;
import ru.devtrifanya.online_store.repositories.FeatureRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
public class FeatureServiceTest {

    private static final int FEATURE_ID = 1;
    private static final int UPDATED_FEATURE_ID = 2;
    private static final int CATEGORY_ID = 1;

    @Mock
    private CategoryService categoryServiceMock;
    @Mock
    private FeatureRepository featureRepoMock;

    @InjectMocks
    private FeatureService testingService;

    @Test
    public void getFeature_featureIsExist_shouldReturnFeature() {
        // Given
        mockFindById_exist();

        // When
        Feature resultFeature = testingService.getFeature(FEATURE_ID);

        // Then
        Mockito.verify(featureRepoMock).findById(FEATURE_ID);
        Assertions.assertEquals(getFeature(FEATURE_ID), resultFeature);
    }

    @Test
    public void getFeature_featureIsNotExist_shouldThrowException() {
        // Given
        mockFindById_notExist();

        // When // Then
        Assertions.assertThrows(NotFoundException.class, () -> testingService.getFeature(FEATURE_ID));
    }

    @Test
    public void getAllFeatures_shouldReturnFeaturesList() {
        // Given
        mockFindAll();

        // When
        List<Feature> resultFeatures = testingService.getAllFeatures();

        // Then
        Mockito.verify(featureRepoMock).findAll();
        Assertions.assertIterableEquals(getFeatures(), resultFeatures);
    }

    @Test
    public void createNewFeature_oneParameter_shouldAssignId() {
        // Given
        Feature featureToSave = getFeature(0);
        mockSaveNew();

        // When
        Feature resultFeature = testingService.createNewFeature(featureToSave);

        // Then
        Mockito.verify(featureRepoMock).save(featureToSave);
        Assertions.assertNotNull(resultFeature.getId());
    }

    @Test
    public void createNewFeature_twoParameters_shouldAssignCategoriesAndId() {
        // Given
        Feature featureToSave = getFeature(0);
        mockGetCategory();
        mockSaveNew();

        // When
        Feature resultFeature = testingService.createNewFeature(featureToSave, CATEGORY_ID);

        // Then
        Mockito.verify(categoryServiceMock).getCategory(CATEGORY_ID);
        Mockito.verify(featureRepoMock).save(featureToSave);
        Assertions.assertIterableEquals(List.of(getCategory(CATEGORY_ID)), resultFeature.getCategories());
        Assertions.assertNotNull(resultFeature.getId());
    }

    @Test
    public void updateFeature_shouldNotChangeCategories() {
        // Given
        Feature updatedFeature = getFeature(UPDATED_FEATURE_ID);
        mockFindById_exist();
        mockSaveUpdated();

        // When
        Feature resultFeature = testingService.updateFeature(updatedFeature);

        // Then
        Mockito.verify(featureRepoMock).findById(UPDATED_FEATURE_ID);
        Mockito.verify(featureRepoMock).save(updatedFeature);
        Assertions.assertIterableEquals(List.of(getCategory(CATEGORY_ID)), resultFeature.getCategories());
    }

    @Test
    public void deleteFeature() {
        // When
        testingService.deleteFeature(FEATURE_ID);

        // Then
        Mockito.verify(featureRepoMock).deleteById(FEATURE_ID);
    }


    // Определение поведения mock-объектов.

    private void mockFindById_exist() {
        Mockito.doAnswer(invocationOnMock -> Optional.of(getFeature(invocationOnMock.getArgument(0))))
                .when(featureRepoMock).findById(anyInt());
    }

    private void mockFindById_notExist() {
        Mockito.when(featureRepoMock.findById(anyInt()))
                .thenReturn(Optional.empty());
    }

    private void mockFindAll() {
        Mockito.when(featureRepoMock.findAll())
                .thenReturn(getFeatures());
    }

    private void mockSaveNew() {
        Mockito.doAnswer(
                invocationOnMock -> {
                    Feature feature = invocationOnMock.getArgument(0);
                    feature.setId(FEATURE_ID);
                    return feature;
                }).when(featureRepoMock).save(any(Feature.class));
    }

    private void mockSaveUpdated() {
        Mockito.doAnswer(invocationOnMock -> invocationOnMock.getArgument(0))
                .when(featureRepoMock).save(any(Feature.class));
    }

    private void mockGetCategory() {
        Mockito.doAnswer(invocationOnMock -> getCategory(invocationOnMock.getArgument(0))).
                when(categoryServiceMock).getCategory(CATEGORY_ID);
    }


    // Вспомогательные методы.

    private Feature getFeature(int featureId) {
        return new Feature()
                .setId(featureId)
                .setCategories(List.of(getCategory(CATEGORY_ID)));
    }

    private Category getCategory(int categoryId) {
        return new Category()
                .setId(categoryId);
    }

    private List<Feature> getFeatures() {
        return List.of(
                new Feature().setId(11),
                new Feature().setId(12),
                new Feature().setId(13)
        );
    }

}
