package ru.devtrifanya.online_store.unit_tests.services;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.devtrifanya.online_store.models.*;
import ru.devtrifanya.online_store.repositories.CategoryRelationRepository;
import ru.devtrifanya.online_store.services.implementations.CategoryRelationService;

@ExtendWith(MockitoExtension.class)
@Data
public class CategoryRelationServiceTest {
    @Mock
    private CategoryRelationRepository categoryRelationRepository;

    @InjectMocks
    private CategoryRelationService categoryRelationService;


    /**
     * Тестируемый метод должен выполнять следующие действия:
     * 1. Инициализировать у сохраняемой характеристики поле parent параметром метода.
     * 2. Вызывать метод categoryRelationRepository.save()
     * 3. Возвращать тот же объект отношения, что был возвращен из метода save(),
     * то есть объект с ненулевым id.
     */
    @Test
    public void createCategoryRelation() {
        int relationId = 1;
        CategoryRelation relationToSave = new CategoryRelation();
        Category expectedParent = new Category();

        CategoryRelation expectedRelation = new CategoryRelation();
        expectedRelation.setParent(expectedParent);

        Mockito.doAnswer(invocationOnMock -> {
                    CategoryRelation relation = invocationOnMock.getArgument(0);
                    relation.setId(relationId);
                    return relation;
                })
                .when(categoryRelationRepository).save(expectedRelation);

        CategoryRelation resultRelation = categoryRelationRepository.save(relationToSave);

        Assertions.assertEquals(expectedRelation, resultRelation);
        Assertions.assertNotNull(resultRelation.getId());

        Mockito.verify(categoryRelationRepository).save(relationToSave);
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
    public void updateCategoryRelation() {
        int expectedId = 1;
        CategoryRelation updatedRelation = new CategoryRelation();
        Category expectedParent = new Category();

        ItemFeature expectedItemFeature = new ItemFeature();
        CategoryRelation expectedRelation = new CategoryRelation();
        expectedRelation.setParent(expectedParent);

        Mockito.when(categoryRelationRepository.save(expectedRelation))
                .thenReturn(expectedRelation);

        CategoryRelation resultRelation = categoryRelationService.updateCategoryRelation(
                expectedId, updatedRelation, expectedParent
        );

        Assertions.assertEquals(expectedId, resultRelation.getId());
        Assertions.assertEquals(expectedParent, resultRelation.getParent());
        Assertions.assertEquals(updatedRelation, resultRelation);
    }
}
