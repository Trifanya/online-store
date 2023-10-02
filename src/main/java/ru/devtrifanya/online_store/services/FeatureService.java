package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.models.Feature;
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
    private final FeatureRepository featureRepository;
    private final ItemFeatureRepository itemFeatureRepository;
    private final CategoryRepository categoryRepository;

    public Feature get(int featureId) {
        Optional<Feature> feature = featureRepository.findById(featureId);
        if (feature.isEmpty()) {
            throw new NotFoundException("Характеристика с таким id не найдена.");
        }
        return feature.get();
    }

    /** Может быть вызван только для конечных категорий. */
    public List<Feature> getAll(int categoryId) {
        return featureRepository.findAllByCategoryId(categoryId);
    }

    public List<ItemFeature> getItemFeatures(int itemId) {
        List<ItemFeature> itemFeatures = itemFeatureRepository.findAllByItemId(itemId);
        return itemFeatures;
    }

    public void create(Feature feature, int categoryId) {
        feature.setCategory(categoryRepository.findById(categoryId).orElse(null));
        featureRepository.save(feature);
    }

    public void update(int featureId, Feature feature) {
        feature.setId(featureId);
        featureRepository.save(feature);
    }

    /** Удаление характеристики категории и всех соответствующих
     * характеристик товаров. */
    public void delete(int featureId) {
        itemFeatureRepository.deleteByFeatureId(featureId);
        featureRepository.deleteById(featureId);
    }
}
