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
    private final FeatureRepository featureRepository;
    private final ItemFeatureRepository itemFeatureRepository;
    private final CategoryRepository categoryRepository;

    /*public Feature getFeature(int featureId) {
        Optional<Feature> feature = featureRepository.findById(featureId);
        if (feature.isEmpty()) {
            throw new NotFoundException("Характеристика с таким id не найдена.");
        }
        return feature.get();
    }*/

    /**
     * Получение всех характеристик категории.
     * Может быть вызван только для конечных категорий.
     */
    /*public List<Feature> getAllFeatures(int categoryId) {
        return featureRepository.findAllByCategoryId(categoryId);
    }*/

    /**
     * Получение всех характеристик конкретного товара.
     */
    /*public List<ItemFeature> getItemFeatures(int itemId) {
        List<ItemFeature> itemFeatures = itemFeatureRepository.findAllByItemId(itemId);
        return itemFeatures;
    }*/

    @Transactional
    public void createNewFeature(Feature feature, int categoryId) {
        feature.setCategory(categoryRepository.findById(categoryId).orElse(null));
        featureRepository.save(feature);
    }

    /**
     * Сохранение новой характеристики товара в таблицу item_feature.
     * Все характеристики товаров сохраняются только при сохранении самого товара.
     */
    @Transactional
    public void createNewItemFeature(ItemFeature itemFeature, Item item, Feature feature) {
        itemFeature.setItem(item);
        itemFeature.setFeature(feature);
        itemFeatureRepository.save(itemFeature);
    }

    /**
     * Сохранение в таблицу item_feature всех характеристик товара из списка itemFeatures.
     */
    @Transactional
    public void createSeveralNewItemFeatures(Item item, List<Feature> features) {
        List<ItemFeature> itemFeaturesToSave = item.getFeatures();
        for (int i = 0; i < itemFeaturesToSave.size(); i++) {
            this.createNewItemFeature(
                    itemFeaturesToSave.get(i),
                    item,
                    features.get(i));
        }
    }

    @Transactional
    public void updateFeatureInfo(int featureId, Feature feature) {
        feature.setId(featureId);
        featureRepository.save(feature);
    }

    /**
     * Апдейт характеристики товара.
     * Все характеристики товаров апдейтсятся только при апдейте самого товара.
     */
    @Transactional
    public void updateItemFeatureInfo(int itemFeatureId, ItemFeature updatedItemFeature, Item item, Feature feature) {
        updatedItemFeature.setId(itemFeatureId);
        updatedItemFeature.setItem(item);
        updatedItemFeature.setFeature(feature);
        itemFeatureRepository.save(updatedItemFeature);
    }

    /**
     * Апдейт всех характеристик товара item.
     */
    @Transactional
    public void updateSeveralItemFeaturesInfo(Item item, List<Feature> features) {
        List<ItemFeature> oldItemFeatures = itemFeatureRepository.findAllByItemId(item.getId());
        List<ItemFeature> updatedItemFeatures = item.getFeatures();
        for (int i = 0; i < updatedItemFeatures.size(); i++) {
            this.updateItemFeatureInfo(
                    oldItemFeatures.get(i).getId(),
                    updatedItemFeatures.get(i),
                    item,
                    features.get(i));
        }
    }

    /** Удаление характеристики категории и всех соответствующих
     * характеристик товаров. */
    @Transactional
    public void deleteFeature(int featureId) {
        itemFeatureRepository.deleteAllByFeatureId(featureId);
        featureRepository.deleteById(featureId);
    }
}
