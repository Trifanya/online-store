package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.models.Feature;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.ItemFeature;
import ru.devtrifanya.online_store.repositories.CategoryRepository;
import ru.devtrifanya.online_store.repositories.FeatureRepository;
import ru.devtrifanya.online_store.repositories.ItemFeatureRepository;
import ru.devtrifanya.online_store.util.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Data
public class FeatureService {
    private final CategoryService categoryService;
    private final ItemFeatureService itemFeatureService;
    private final FeatureRepository featureRepository;

    @Transactional
    public Feature createNewFeature(Feature feature, int categoryId) {
        feature.setCategory(categoryService.getCategory(categoryId));
        return featureRepository.save(feature);
    }

    @Transactional
    public Feature updateFeatureInfo(int featureId, Feature feature) {
        feature.setId(featureId);
        return featureRepository.save(feature);
    }

    /**
     * Удаление характеристики категории и всех соответствующих
     * характеристик товаров.
     */
    @Transactional
    public void deleteFeature(int featureId) {
        itemFeatureService.deleteItemFeaturesByFeatureId(featureId);
        featureRepository.deleteById(featureId);
    }
}
