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
import ru.devtrifanya.online_store.repositories.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    private static final int PARENT_ID = 11;
    private static final int NEW_PARENT_ID = 12;
    private static final int CATEGORY_ID = 1;
    private static final int UPDATED_CATEGORY_ID = 2;

    private static final int[] FEATURE_IDS = {1, 2, 3};

    @Mock
    private FeatureService featureServiceMock;
    @Mock
    private CategoryRepository categoryRepoMock;

    @InjectMocks
    private CategoryService testingService;

    @Test
    public void getCategory_categoryIsExist_shouldReturnCategory() {
        // Given
        mockFindById_existAndFinal();

        // When
        Category resultCategory = testingService.getCategory(CATEGORY_ID);

        // Then
        Mockito.verify(categoryRepoMock).findById(CATEGORY_ID);
        Assertions.assertEquals(getCategory(CATEGORY_ID), resultCategory);
    }

    @Test
    public void getCategory_categoryIsNotExist_shouldThrowException() {
        // Given
        mockFindById_notExist();

        // When // Then
        Assertions.assertThrows(NotFoundException.class, () -> testingService.getCategory(CATEGORY_ID));
    }

    @Test
    public void getRootCategories_shouldReturnListOfCategories() {
        // Given
        mockFindRootCategories();

        // When
        List<Category> resultCategories = testingService.getRootCategories();

        // Then
        Mockito.verify(categoryRepoMock).findRootCategories();
        Assertions.assertIterableEquals(getCategories(), resultCategories);
    }

    @Test
    public void createNewCategory_shouldAssignIdFeaturesAndParents() {
        // Given
        Category categoryToSave = getCategory(CATEGORY_ID);
        mockGetFeature();
        mockFindById_existAndFinal();
        mockSaveNew();

        // When
        Category resultCategory = testingService.createNewCategory(
                categoryToSave, FEATURE_IDS, PARENT_ID
        );

        // Then
        Mockito.verify(categoryRepoMock).save(categoryToSave);
        Mockito.verify(featureServiceMock, Mockito.times(FEATURE_IDS.length)).getFeature(anyInt());
        Mockito.verify(categoryRepoMock).findById(PARENT_ID);
        Assertions.assertNotNull(resultCategory.getId());
        Assertions.assertIterableEquals(getFeatures(), resultCategory.getFeatures());
        Assertions.assertIterableEquals(List.of(getCategory(PARENT_ID)), resultCategory.getParents());
    }


    @Test
    public void updateCategoryInfo_categoryIsReplaced_shouldAssignNewParent() {
        // Given
        mockFindById_existAndFinal();
        mockGetFeature();
        mockSaveUpdated();

        // When
        Category resultCategory = testingService.updateCategory(
                getCategory(UPDATED_CATEGORY_ID), FEATURE_IDS, PARENT_ID, NEW_PARENT_ID
        );

        // Then
        Mockito.verify(categoryRepoMock, Mockito.times(3)).findById(anyInt());
        Assertions.assertTrue(resultCategory.getParents().contains(getCategory(NEW_PARENT_ID)));
        Assertions.assertFalse(resultCategory.getParents().contains(getCategory(PARENT_ID)));
    }

    @Test
    public void updateCategoryInfo_categoryIsNotReplaced_shouldNotAssignNewParent() {
        // Given
        mockFindById_existAndFinal();
        mockGetFeature();
        mockSaveUpdated();

        // When
        Category resultCategory = testingService.updateCategory(
                getCategory(UPDATED_CATEGORY_ID), FEATURE_IDS, PARENT_ID, PARENT_ID
        );
        // Then
        Mockito.verify(categoryRepoMock, Mockito.times(1)).findById(UPDATED_CATEGORY_ID);
        Assertions.assertTrue(resultCategory.getParents().contains(getCategory(PARENT_ID)));
    }

    @Test
    public void updateCategoryInfo_shouldAssignFeaturesWithSpecifiedIds() {
        // Given
        mockFindById_existAndFinal();
        mockGetFeature();
        mockSaveUpdated();

        // When
        Category resultCategory = testingService.updateCategory(
                getCategory(UPDATED_CATEGORY_ID), FEATURE_IDS, PARENT_ID, PARENT_ID
        );

        // Then
        Mockito.verify(featureServiceMock, Mockito.times(FEATURE_IDS.length)).getFeature(anyInt());
        Assertions.assertIterableEquals(getFeatures(), resultCategory.getFeatures());
    }

    @Test
    public void deleteCategory_categoryIsFinal_shouldDeleteCategory() {
        // Given
        mockFindById_existAndFinal();

        // When
        testingService.deleteCategory(CATEGORY_ID);

        // Then
        Mockito.verify(categoryRepoMock, times(1)).findById(CATEGORY_ID);
        Mockito.verify(categoryRepoMock).deleteById(CATEGORY_ID);
        Mockito.verify(categoryRepoMock, never()).saveAll(any());
    }

    @Test
    public void deleteCategory_categoryIsNotFinal_shouldUpdateChildrenAndParents() {
        // Given
        mockFindById_existAndNotFinal();

        // When
        testingService.deleteCategory(CATEGORY_ID);

        // Then
        Mockito.verify(categoryRepoMock, times(2)).findById(CATEGORY_ID);
        Mockito.verify(categoryRepoMock).deleteById(CATEGORY_ID);
        Mockito.verify(categoryRepoMock).saveAll(any());
    }


    // Определение поведения mock-объектов.

    private void mockFindById_existAndFinal() {
        Mockito.doAnswer(
                invocationOnMock -> Optional.of(
                        getCategory(invocationOnMock.getArgument(0))
                                .setParents(new ArrayList<>(List.of(getCategory(PARENT_ID))))
                                .setChildren(new ArrayList<>())
                )).when(categoryRepoMock).findById(anyInt());
    }

    private void mockFindById_existAndNotFinal() {
        Mockito.doAnswer(
                invocationOnMock -> Optional.of(
                        getCategory(invocationOnMock.getArgument(0))
                                .setParents(new ArrayList<>(List.of(getCategory(PARENT_ID))))
                                .setChildren(getCategories())
                )).when(categoryRepoMock).findById(anyInt());
    }

    private void mockFindById_notExist() {
        Mockito.when(categoryRepoMock.findById(anyInt()))
                .thenReturn(Optional.empty());
    }

    private void mockFindRootCategories() {
        Mockito.when(categoryRepoMock.findRootCategories())
                .thenReturn(getCategories());
    }

    private void mockSaveNew() {
        Mockito.doAnswer(
                invocationOnMock -> {
                    Category category = invocationOnMock.getArgument(0);
                    category.setId(CATEGORY_ID);
                    return category;
                }).when(categoryRepoMock).save(any(Category.class));
    }

    private void mockSaveUpdated() {
        Mockito.doAnswer(invocationOnMock -> invocationOnMock.getArgument(0))
                .when(categoryRepoMock).save(any(Category.class));
    }

    private void mockGetFeature() {
        Mockito.doAnswer(invocationOnMock -> getFeature(invocationOnMock.getArgument(0)))
                .when(featureServiceMock).getFeature(anyInt());
    }


    // Вспомогательные методы.

    private Category getCategory(int categoryId) {
        return new Category()
                .setId(categoryId)
                .setParents(new ArrayList<>())
                .setChildren(new ArrayList<>());
    }

    private Feature getFeature(int featureId) {
        return new Feature()
                .setId(featureId);
    }

    private List<Category> getCategories() {
        return List.of(
                new Category().setId(11),
                new Category().setId(12),
                new Category().setId(13)
        );
    }

    private List<Feature> getFeatures() {
        return List.of(
                new Feature().setId(FEATURE_IDS[0]),
                new Feature().setId(FEATURE_IDS[1]),
                new Feature().setId(FEATURE_IDS[2])
        );
    }
}
