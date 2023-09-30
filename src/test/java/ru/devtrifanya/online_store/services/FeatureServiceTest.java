package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.devtrifanya.online_store.repositories.CategoryRepository;
import ru.devtrifanya.online_store.repositories.FeatureRepository;
import ru.devtrifanya.online_store.repositories.ItemFeatureRepository;

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

    @Test
    public void get_featureIsExist_shouldReturnFeatureById() {
        // given

        // when

        // then
    }

    @Test
    public void get_featureNotExist_shouldThrowNotFoundException() {
        // given

        // when

        // then
    }

    @Test
    public void getAll_categoryIsFinal_shouldReturnFeaturesByCategoryId() {
        // given

        // when

        // then
    }

    @Test
    public void getAll_categoryIsNotFinal_shouldThrowNotFoundException() {
        // given

        // when

        // then
    }

    @Test
    public void create_categoryIsFinal_shouldSaveFeature() {
        // given

        // when

        // then
    }

    @Test
    public void create_categoryIsNotFinal_shouldThrowUnavailableActionException() {
        // given

        // when

        // then
    }

    @Test
    public void update_shouldUpdateFeatureName() {
        // given

        // when

        // then
    }

    @Test
    public void delete_shouldDeleteFeatureById() {
        // given

        // when

        // then
    }

    @Test
    public void delete_shouldDeleteItemFeaturesByFeatureId() {
        // given

        // when

        // then
    }

}
