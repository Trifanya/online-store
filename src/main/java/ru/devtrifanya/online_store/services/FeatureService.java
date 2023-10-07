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
    private final CategoryService categoryService;
    private final ItemFeatureService itemFeatureService;
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
        feature.setCategory(category);
        feature.setFeatures(new ArrayList<>()); // данное поле инициализируется пустым списком, т.к. при создании новой характеристики категории конкретные характеристики не указываются
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
    public Feature updateFeatureInfo(int featureId, Feature feature, Category category) {
        feature.setId(featureId);
        feature.setCategory(category);
        feature.setFeatures(itemFeatureService.getItemFeaturesByFeatureId(featureId)); // данное поле инициализируется списком уже существующих характеристик товаров
        return featureRepository.save(feature);
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
