package ru.devtrifanya.online_store.unit_tests.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.devtrifanya.online_store.models.Feature;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.services.FeatureService;
import ru.devtrifanya.online_store.services.CategoryService;
import ru.devtrifanya.online_store.exceptions.NotFoundException;
import ru.devtrifanya.online_store.repositories.CategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private FeatureService featureServiceMock;
    @Mock
    private CategoryRepository categoryRepositoryMock;

    @InjectMocks
    private CategoryService testingService;

    private int parentId = 1;
    private int newParentId = 2;
    private int foundCategoryId = 3;
    private int savedCategoryId = 3;
    private int updatedCategoryId = 3;
    private int categoryToDeleteId = 3;

    private int[] featureIDs = {1, 2, 3};

    private Category parent = new Category(parentId, "parent");
    private Category newParent = new Category(newParentId, "new parent");
    private Category foundCategory = new Category(foundCategoryId, "found");
    private Category categoryToSave = new Category(0, "new");
    private Category updatedCategory = new Category(updatedCategoryId, "updated");

    private List<Category> categories = List.of(
            new Category(1, "category1"),
            new Category(2, "category2"),
            new Category(3, "category3")
    );

    private List<Feature> features = List.of(
            new Feature(featureIDs[0], "feature1", "feature1", "unit1"),
            new Feature(featureIDs[1], "feature2", "feature2", "unit2"),
            new Feature(featureIDs[2], "feature3", "feature3", "unit3")
    );

    @Test
    public void getCategory_categoryIsExist_shouldReturnCategory() {
        // Определение поведения mock-объектов
        Mockito.when(categoryRepositoryMock.findById(foundCategoryId))
                .thenReturn(Optional.of(foundCategory));

        // Выполнение тестируемого метода
        Category resultCategory = testingService.getCategory(foundCategoryId);
        // Проверка совпадения ожидаемых результатов с реальными
        Mockito.verify(categoryRepositoryMock).findById(foundCategoryId);
        Assertions.assertEquals(foundCategory, resultCategory);
    }

    @Test
    public void getCategory_categoryIsNotExist_shouldThrowException() {
        // Определение поведения mock-объектов
        Mockito.when(categoryRepositoryMock.findById(foundCategoryId))
                .thenReturn(Optional.empty());

        // Выполнение тестируемого метода и проверка совпадения ожидаемых результатов с реальными
        Assertions.assertThrows(
                NotFoundException.class,
                () -> testingService.getCategory(foundCategoryId)
        );
        Mockito.verify(categoryRepositoryMock).findById(foundCategoryId);
    }

    @Test
    public void getRootCategories_shouldReturnListOfCategories() {
        // Определение поведения mock-объектов
        Mockito.when(categoryRepositoryMock.findRootCategories())
                .thenReturn(categories);

        // Выполнение тестируемого метода
        List<Category> resultCategories = testingService.getRootCategories();
        // Проверка совпадения ожидаемых результатов с реальными
        Mockito.verify(categoryRepositoryMock).findRootCategories();
        Assertions.assertIterableEquals(categories, resultCategories);
    }

    @Test
    public void createNewCategory_shouldAssignId() {
        // Определение поведения mock-объектов
        createNewCategory_determineBehaviourOfMocks();

        // Выполнение тестируемого метода
        Category resultCategory = testingService.createNewCategory(
                categoryToSave, featureIDs, parentId
        );
        // Проверка совпадения ожидаемых результатов с реальными
        Mockito.verify(categoryRepositoryMock).save(categoryToSave);
        Assertions.assertNotNull(resultCategory.getId());
    }

    @Test
    public void createNewCategory_shouldAssignFeatures() {
        // Определение поведения mock-объектов
        createNewCategory_determineBehaviourOfMocks();

        Category resultCategory = testingService.createNewCategory(
                categoryToSave, featureIDs, parentId
        );
        Mockito.verify(featureServiceMock, Mockito.times(featureIDs.length)).getFeature(anyInt());
        Assertions.assertIterableEquals(features, resultCategory.getFeatures());
    }

    @Test
    public void createNewCategory_shouldAssignParentCategory() {
        // Определение поведения mock-объектов
        createNewCategory_determineBehaviourOfMocks();

        // Выполнение тестируемого метода
        Category resultCategory = testingService.createNewCategory(
                categoryToSave, featureIDs, parentId
        );
        // Проверка соответствия ожидаемых результатов с реальными
        Mockito.verify(categoryRepositoryMock).findById(parentId);
        Assertions.assertEquals(parentId, resultCategory.getParents().get(0).getId());
        Assertions.assertEquals(1, resultCategory.getParents().size());
    }

    @Test
    public void updateCategoryInfo_categoryIsReplaced_shouldAssignNewParent() {
        // Определение поведения mock-объектов
        updateCategoryInfo_determineBehaviourOfMocks();

        // Выполнение тестируемого метода
        Category resultCategory = testingService.updateCategoryInfo(
                updatedCategory, featureIDs, parentId, newParentId
        );
        // Проверка соответствия ожидаемых результатов с реальными
        Mockito.verify(categoryRepositoryMock, Mockito.times(3)).findById(anyInt());
        Assertions.assertTrue(resultCategory.getParents().contains(newParent));
        Assertions.assertFalse(resultCategory.getParents().contains(parent));
    }

    @Test
    public void updateCategoryInfo_categoryIsNotReplaced_shouldNotAssignNewParent() {
        // Определение поведения mock-объектов
        updateCategoryInfo_determineBehaviourOfMocks();

        // Выполнение тестируемого метода
        Category resultCategory = testingService.updateCategoryInfo(
                updatedCategory, featureIDs, parentId, parentId
        );
        // Проверка соответствия ожидаемых результатов с реальными
        Mockito.verify(categoryRepositoryMock, Mockito.times(1)).findById(updatedCategoryId);
        Assertions.assertTrue(resultCategory.getParents().contains(parent));
    }

    @Test
    public void updateCategoryInfo_shouldAssignFeaturesWithSpecifiedIds() {
        // Определение поведения mock-объектов
        updateCategoryInfo_determineBehaviourOfMocks();

        // Выполнение тестируемого метода
        Category resultCategory = testingService.updateCategoryInfo(
                updatedCategory, featureIDs, parentId, parentId
        );
        // Проверка совпадения ожидаемых результатов с реальными
        Mockito.verify(featureServiceMock, Mockito.times(featureIDs.length)).getFeature(anyInt());
        Assertions.assertIterableEquals(features, resultCategory.getFeatures());
    }

    @Test
    public void updateCategoryInfo_shouldSaveUpdatedCategory() {
        // Определение поведения mock-объектов
        updateCategoryInfo_determineBehaviourOfMocks();

        // Выполнение тестируемого метода
        Category resultCategory = testingService.updateCategoryInfo(
                updatedCategory, featureIDs, parentId, parentId
        );
        // Проверка совпадения ожидаемых результатов с реальными
        Mockito.verify(categoryRepositoryMock).save(updatedCategory);
        Assertions.assertEquals(resultCategory, updatedCategory);
    }

    @Test
    public void deleteCategory_categoryIsFinal_shouldDeleteCategory() {
        Category categoryToDelete = new Category(categoryToDeleteId, foundCategory.getName());
        categoryToDelete.setChildren(new ArrayList<>());

        // Определение поведения mock-объектов
        Mockito.when(categoryRepositoryMock.findById(categoryToDeleteId))
                .thenReturn(Optional.of(categoryToDelete));

        // Выполнение тестируемого метода
        testingService.deleteCategory(categoryToDeleteId);
        // Проверка соответствия ожидаемых вызовов методов реальным
        Mockito.verify(categoryRepositoryMock, times(1)).findById(categoryToDeleteId);
        Mockito.verify(categoryRepositoryMock).deleteById(categoryToDeleteId);
        Mockito.verify(categoryRepositoryMock, never()).saveAll(any());
    }

    @Test
    public void deleteCategory_categoryIsNotFinal_shouldUpdateChildrenAndParents() {
        Category categoryToDelete = new Category(categoryToDeleteId, foundCategory.getName());
        categoryToDelete.setParents(List.of(new Category()));
        categoryToDelete.getParents().get(0).setChildren(new ArrayList<>(List.of(new Category())));
        categoryToDelete.setChildren(List.of(new Category()));
        categoryToDelete.getChildren().get(0).setParents(new ArrayList<>(List.of(new Category())));

        Mockito.when(categoryRepositoryMock.findById(categoryToDeleteId))
                .thenReturn(Optional.of(categoryToDelete));

        testingService.deleteCategory(categoryToDeleteId);

        Mockito.verify(categoryRepositoryMock, times(2)).findById(categoryToDeleteId);
        Mockito.verify(categoryRepositoryMock).deleteById(categoryToDeleteId);
        Mockito.verify(categoryRepositoryMock).saveAll(any());
    }

    public void createNewCategory_determineBehaviourOfMocks() {
        Mockito.when(categoryRepositoryMock.findById(parentId))
                .thenReturn(Optional.of(parent));

        Mockito.doAnswer(
                invocationOnMock -> {
                    int id = invocationOnMock.getArgument(0);
                    return features.stream()
                            .filter(feature -> feature.getId() == id)
                            .findFirst().orElse(null);
                }).when(featureServiceMock).getFeature(anyInt());

        Mockito.doAnswer(
                invocationOnMock -> {
                    Category category = invocationOnMock.getArgument(0);
                    category.setId(savedCategoryId);
                    return category;
                }).when(categoryRepositoryMock).save(any(Category.class));
    }

    public void updateCategoryInfo_determineBehaviourOfMocks() {
        Mockito.doAnswer(
                invocationOnMock -> {
                    int id = invocationOnMock.getArgument(0);
                    Category category = List.of(foundCategory, parent, newParent)
                            .stream()
                            .filter(cat -> cat.getId() == id)
                            .findFirst().orElse(null);
                    if (category == foundCategory) {
                        category.setParents(new ArrayList<>(List.of(parent)));
                    }
                    return Optional.of(category);
                }).when(categoryRepositoryMock).findById(anyInt());

        Mockito.doAnswer(
                invocationOnMock -> {
                    int id = invocationOnMock.getArgument(0);
                    return features.stream()
                            .filter(feature -> feature.getId() == id)
                            .findFirst().orElse(null);
                }).when(featureServiceMock).getFeature(anyInt());

        Mockito.doAnswer(invocationOnMock -> invocationOnMock.getArgument(0))
                .when(categoryRepositoryMock).save(any(Category.class));
    }
}
