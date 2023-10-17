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
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.repositories.CategoryRepository;
import ru.devtrifanya.online_store.services.CategoryRelationService;
import ru.devtrifanya.online_store.services.CategoryService;
import ru.devtrifanya.online_store.services.FeatureService;
import ru.devtrifanya.online_store.services.ItemService;
import ru.devtrifanya.online_store.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@Data
public class CategoryServiceTest {

    @Mock
    private FeatureService featureService;
    @Mock
    private ItemService itemService;
    @Mock
    private CategoryRelationService categoryRelationService;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;


    @Test
    public void getCategory_categoryIsExist_shouldSetCategoryItemsListAndReturnCategory() {
        int categoryId = 1;
        int pageNum = 0;
        int itemsPerPage = 5;
        String sortBy = "id";
        Category expectedCategory = new Category();
        List<Item> expectedItemsList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            expectedItemsList.add(new Item());
            expectedItemsList.get(i).setId(i + 1);
        }

        Mockito.when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(expectedCategory));
        Mockito.when(itemService.getItemsByCategory(categoryId, pageNum, itemsPerPage, sortBy))
                .thenReturn(expectedItemsList);

        Category resultCategory = categoryService.getCategory(categoryId, pageNum, itemsPerPage, sortBy);

        Assertions.assertEquals(expectedCategory, resultCategory);
        Assertions.assertEquals(expectedItemsList, resultCategory.getItems());

        Mockito.verify(categoryRepository).findById(categoryId);
        Mockito.verify(itemService).getItemsByCategory(categoryId, pageNum, itemsPerPage, sortBy);
    }

    @Test
    public void getCategory_categoryIsNotExist_shouldThrowNotFoundException() {
        int categoryId = 1;
        int pageNum = 0;
        int itemsPerPage = 5;
        String sortBy = "id";

        Mockito.when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                NotFoundException.class,
                () -> categoryService.getCategory(categoryId, pageNum, itemsPerPage, sortBy)
        );
        Mockito.verify(categoryRepository).findById(categoryId);
    }

    /**
     * В данном методе должны выполняться следующие действия:
     * 1) Категории должен присваиваться id (который изначально равен 0).
     * 2) Должен вызываться метод, сохраняющий характеристики сохраняемой категории.
     * 3) Должен вызываться метод, сохраняющий товары данной категории.
     * 4) Должен вызываться метод, сохраняющий список отношений, где сохраняемая категория является дочерней
     *    и где сохраняемая категория является родительской.
     * 5) Метод должен возвращать ту же категорию, что была передана в метод.
     */
    @Test
    public void createNewCategory_parentIsExist_shouldSetCategoryIdAndFeaturesAndItemsAndRelations() {
        int categoryId = 1;
        int parentCategoryId = 2;
        Category categoryToSave = new Category();
        Category parentCategory = new Category();

        Mockito.when(featureService.)

        Assertions.assertNotNull(resultCategory.getId());
        Assertions.assertEquals(resultCategory, categoryToSave);


    }

}
