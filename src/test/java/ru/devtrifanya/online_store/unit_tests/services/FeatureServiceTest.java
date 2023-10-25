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
import ru.devtrifanya.online_store.repositories.FeatureRepository;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class FeatureServiceTest {
    @Mock
    private CategoryService categoryServiceMock;
    @Mock
    private FeatureRepository featureRepoMock;

    @InjectMocks
    private FeatureService testingService;

    private int featureId = 1;
    private int savedFeatureId = 2;
    private int updatedFeatureId = 2;

    private int categoryId = 1;

    private Feature foundFeature = new Feature(featureId, "found", "found", "unit1");
    private Feature featureToSave = new Feature(0, "new", "new", "unit2");
    private Feature updatedFeature = new Feature(updatedFeatureId, "updated", "updated", "unit3");

    private Category foundCategory = new Category(1, "found");

    private List<Feature> features = List.of(
        new Feature(10, "f10", "f10", "u10"),
        new Feature(11, "f11", "f11", "u11"),
        new Feature(12, "f12", "f12", "u12")
    );

    private List<Category> categories = List.of(
            new Category(10, "category10"),
            new Category(11, "category11"),
            new Category(12, "category12")
    );

    @Test
    public void getFeature_featureIsExist_shouldReturnFeature() {
        // Определение поведения mock-объектов
        Mockito.when(featureRepoMock.findById(featureId))
                .thenReturn(Optional.of(foundFeature));

        // Вызов тестируемого метода
        Feature resultFeature = testingService.getFeature(featureId);

        // Проверка совпадения ожидаемого результата с реальным
        Mockito.verify(featureRepoMock).findById(featureId);
        Assertions.assertEquals(foundFeature, resultFeature);
    }

    @Test
    public void getFeature_featureIsNotExist_shouldThrowException() {
        // Определение поведения mock-объектов
        Mockito.when(featureRepoMock.findById(featureId))
                .thenReturn(Optional.empty());

        // Вызов тестируемого метода и проверка совпадения ожидаемого результата с реальным
        Assertions.assertThrows(
                NotFoundException.class,
                () -> testingService.getFeature(featureId)
        );
        Mockito.verify(featureRepoMock).findById(featureId);
    }

    @Test
    public void getAllFeatures_shouldReturnFeaturesList() {
        // Определение поведения mock-объектов
        Mockito.when(featureRepoMock.findAll())
                .thenReturn(features);

        // Выполнение тестируемого метода
        List<Feature> resultFeatures = testingService.getAllFeatures();

        // Проверка совпадения ожидаемого результата с реальным
        Mockito.verify(featureRepoMock).findAll();
        Assertions.assertIterableEquals(features, resultFeatures);
    }

    @Test
    public void createNewFeature_oneParameter_shouldAssignId() {
        // Определение поведения mock-объектов
        createNewFeature_determineBehaviorOfSaveMethod();

        // Выполнение тестируемого метода
        Feature resultFeature = testingService.createNewFeature(featureToSave);

        // Проверка совпадения ожидаемого результата с реальным
        Mockito.verify(featureRepoMock).save(featureToSave);
        Assertions.assertNotNull(resultFeature.getId());
    }

    @Test
    public void createNewFeature_twoParameters_shouldAssignCategories() {
        // Определение поведения mock-объектов
        Mockito.when(categoryServiceMock.getCategory(categoryId))
                        .thenReturn(foundCategory);
        createNewFeature_determineBehaviorOfSaveMethod();

        // Выполнение тестируемого метода
        Feature resultFeature = testingService.createNewFeature(featureToSave, categoryId);

        // Проверка совпадения ожидаемого результата с реальным
        Mockito.verify(categoryServiceMock).getCategory(categoryId);
        Assertions.assertIterableEquals(List.of(foundCategory), resultFeature.getCategories());
    }

    @Test
    public void createNewFeature_twoParameters_shouldAssignId() {
        // Определение поведения mock-объектов
        Mockito.when(categoryServiceMock.getCategory(categoryId))
                .thenReturn(foundCategory);
        createNewFeature_determineBehaviorOfSaveMethod();

        // Выполнение тестируемого метода
        Feature resultFeature = testingService.createNewFeature(featureToSave, categoryId);

        // Проверка совпадения ожидаемого результата с реальным
        Mockito.verify(featureRepoMock).save(featureToSave);
        Assertions.assertNotNull(resultFeature.getId());
    }

    @Test
    public void updateFeatureInfo_shouldNotChangeCategories() {
        Feature updatedFeatureWithCategories = new Feature(updatedFeatureId, updatedFeature.getName(),
                updatedFeature.getRequestParamName(), updatedFeature.getUnit());
        updatedFeature.setCategories(new ArrayList<>(categories));

        // Определение поведения mock-объектов
        Mockito.when(featureRepoMock.findById(updatedFeatureId))
                .thenReturn(Optional.of(updatedFeature));
        Mockito.doAnswer(invocationOnMock -> invocationOnMock.getArgument(0))
                .when(featureRepoMock).save(any(Feature.class));

        // Выполнение тестируемого метода
        Feature resultFeature = testingService.updateFeatureInfo(this.updatedFeature);
        // Проверка совпадения ожидаемого результата с реальным
        Mockito.verify(featureRepoMock).findById(updatedFeatureId);
        Mockito.verify(featureRepoMock).save(updatedFeature);
        Assertions.assertIterableEquals(categories, resultFeature.getCategories());
    }

    @Test
    public void deleteFeature() {
        testingService.deleteFeature(featureId);

        Mockito.verify(featureRepoMock).deleteById(featureId);
    }

    public void createNewFeature_determineBehaviorOfSaveMethod() {
        Mockito.doAnswer(invocationOnMock -> {
                    Feature feature = invocationOnMock.getArgument(0);
                    feature.setId(savedFeatureId);
                    return feature;
                }).when(featureRepoMock).save(any(Feature.class));
    }
}
