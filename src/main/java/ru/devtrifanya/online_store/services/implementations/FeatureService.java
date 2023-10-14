package ru.devtrifanya.online_store.services.implementations;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.models.Feature;
import ru.devtrifanya.online_store.repositories.FeatureRepository;
import ru.devtrifanya.online_store.exceptions.NotFoundException;

@Service
@Transactional(readOnly = true)
@Data
public class FeatureService {
    private final CategoryService categoryService;

    private final FeatureRepository featureRepository;

    @Autowired
    public FeatureService(@Lazy CategoryService categoryService,
                          FeatureRepository featureRepository) {
        this.categoryService = categoryService;
        this.featureRepository = featureRepository;
    }

    public Feature getFeature(int featureId) {
        return featureRepository.findById(featureId)
                .orElseThrow(() -> new NotFoundException("Характеристика с указанным id не найдена."));
    }

    /**
     * Добавление новой характеристики категории.
     */
    @Transactional
    //public Feature createNewFeature(Feature feature, Category category) {
    public Feature createNewFeature(Feature featureToSave, int categoryId) {
        Category category = categoryService.getCategory(categoryId);
        featureToSave.getCategories().add(category);
        return featureRepository.save(featureToSave);
    }

    /**
     * Обновление характеристики категории.
     */
    @Transactional
    //public Feature updateFeatureInfo(Feature updatedFeature, Category category) {
    public Feature updateFeatureInfo(Feature updatedFeature/*, int categoryId*/) {
        /*Category category = categoryService.getCategory(categoryId);
        updatedFeature.getCategories().add(category);*/

        return featureRepository.save(updatedFeature);
    }

    /**
     * Удаление характеристики категории.
     */
    @Transactional
    public void deleteFeature(int featureId) {
        featureRepository.deleteById(featureId);
    }
}
