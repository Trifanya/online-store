package ru.devtrifanya.online_store.services;

import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;

import ru.devtrifanya.online_store.models.Feature;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.exceptions.NotFoundException;
import ru.devtrifanya.online_store.repositories.FeatureRepository;

import java.util.Collections;
import java.util.List;

@Service
public class FeatureService {
    private final CategoryService categoryService;

    private final FeatureRepository featureRepository;

    @Autowired
    public FeatureService(@Lazy CategoryService categoryService,
                          FeatureRepository featureRepository) {
        this.categoryService = categoryService;
        this.featureRepository = featureRepository;
    }

    /**
     * Получение характеристики категории по ее id.
     */
    public Feature getFeature(int featureId) {
        return featureRepository.findById(featureId)
                .orElseThrow(() -> new NotFoundException("Характеристика с указанным id не найдена."));
    }

    /**
     * Получение списка всех характеристик категорий.
     */
    public List<Feature> getAllFeatures() {
        return featureRepository.findAll();
    }

    /**
     * Добавление новой характеристики в общий список характеристик.
     * Данный метод предназначен для добавления новой характеристики со страницы
     * с общим списком характеристик.
     */
    public Feature createNewFeature(Feature featureToSave) {
        return featureRepository.save(featureToSave);
    }

    /**
     * Добавление новой характеристики в общий список характеристик и назначение
     * категории с указанным id.
     * Данный метод предназначен для добавления новой характеристики со страницы
     * добавления или редактирования категории товаров.
     */
    public Feature createNewFeature(Feature featureToSave, int categoryId) {
        Category category = categoryService.getCategory(categoryId);

        featureToSave.setCategories(Collections.singletonList(category));

        return featureRepository.save(featureToSave);
    }

    /**
     * Обновление характеристики категории.
     */
    public Feature updateFeatureInfo(Feature updatedFeature) {
        return featureRepository.save(updatedFeature);
    }

    /**
     * Удаление характеристики категории.
     */
    public void deleteFeature(int featureId) {
        featureRepository.deleteById(featureId);
    }
}
