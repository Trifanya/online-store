package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.models.Feature;
import ru.devtrifanya.online_store.repositories.CategoryRepository;
import ru.devtrifanya.online_store.repositories.FeatureRepository;
import ru.devtrifanya.online_store.repositories.ItemFeatureRepository;

@Service
@Transactional(readOnly = true)
@Data
public class FeatureService {
    private final FeatureRepository featureRepository;
    private final ItemFeatureRepository itemFeatureRepository;
    private final CategoryRepository categoryRepository;

    public void create(Feature feature, int categoryId) {
        feature.setCategory(categoryRepository.findById(categoryId).orElse(null));
        featureRepository.save(feature);
    }

    public void update(int id, Feature feature) {
        feature.setId(id);
        featureRepository.save(feature);
    }

    public void delete(int id) {
        featureRepository.deleteById(id);
    }
}
