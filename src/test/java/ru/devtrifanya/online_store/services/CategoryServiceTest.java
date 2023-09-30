package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.devtrifanya.online_store.repositories.CategoryRelationRepository;
import ru.devtrifanya.online_store.repositories.CategoryRepository;
import ru.devtrifanya.online_store.repositories.ItemRepository;

@ExtendWith(MockitoExtension.class)
@Data
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryRelationRepository categoryRelationRepository;
    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private CategoryService categoryService;


    @Test
    public void getAll_shouldReturnAllSubcategoriesBySupercategoryId() {
        // given

        // when

        // then
    }

    @Test
    public void create_shouldSaveCategory() {
        // given

        // when

        // then
    }

    @Test
    public void create_shouldSaveCategoryRelation() {
        // given

        // when

        // then
    }

    @Test
    public void update_shouldUpdateCategoryNameById() {
        // given

        // when

        // then
    }

    @Test
    public void delete_shouldDeleteCategoryById() {
        // given

        // when

        // then
    }

    @Test
    public void delete_shouldDeleteCategoryRelationByParentId() {
        // given

        // when

        // then
    }

    @Test
    public void delete_shouldDeleteFeaturesByCategoryId() {
        // given

        // when

        // then
    }

    @Test
    public void delete_categoryIsFinal_shouldDeleteItemsByCategoryId() {
        // given

        // when

        // then
    }

    @Test
    public void delete_categoryIsIntermediate_shouldUpdateParentsOfSubcategories() {
        // given

        // when

        // then
    }
}
