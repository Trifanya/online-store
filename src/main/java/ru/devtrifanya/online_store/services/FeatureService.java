package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.models.Feature;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.ItemFeature;
import ru.devtrifanya.online_store.repositories.CategoryRepository;
import ru.devtrifanya.online_store.repositories.FeatureRepository;
import ru.devtrifanya.online_store.repositories.ItemFeatureRepository;
import ru.devtrifanya.online_store.util.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Data
public class FeatureService {
    private final FeatureRepository featureRepository;

    public List<Feature> getFeaturesByCategoryId(int categoryId) {
        return featureRepository.findAllByCategoryId(categoryId);
    }

    /**
     * Добавление новой характеристики категории.
     * Метод получает на вход характеристику, у которой проинициализировано только
     * поле name, инициализирует поле category и features, затем вызывает метод репозитория
     * для сохранения характеристики в БД и возвращает сохраненную характеристику.
     */
    @Transactional
    public Feature createNewFeature(Feature feature, Category category) {
        feature.getCategories().add(category);
        return featureRepository.save(feature);
    }

    /**
     * Изменение названия характеристики категории.
     * Метод получает на вход id характеристики, которая будет изменена, характеристику,
     * у которой проинициализировано только поле name, инициализирует поле category и
     * features, затем вызывает метод репозитория для сохранения характеристики в БД и
     * возвращает сохраненную характеристику.
     */
    @Transactional
    public Feature updateFeatureInfo(int featureId, Feature updatedFeature, Category category) {
        Feature oldFeature = featureRepository.findById(featureId)
                        .orElseThrow(() -> new NotFoundException("Характеристика с указанным id не найдена."));

        updatedFeature.setCategories(oldFeature.getCategories());
        updatedFeature.getCategories().add(category);
        updatedFeature.setId(featureId);

        return featureRepository.save(updatedFeature);
    }

    /**
     * Удаление характеристики категории.
     * Метод получает на вход id характеристики, которую нужно удалить и вызывает метод
     * репозитория для ее удаления.
     */
    @Transactional
    public void deleteFeature(int featureId) {
        featureRepository.deleteById(featureId);
    }
}
