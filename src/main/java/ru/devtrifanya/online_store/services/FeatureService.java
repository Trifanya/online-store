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

    @Transactional
    public Feature createNewFeature(Feature feature, int categoryId) {
        feature.setCategory(categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с указанным id не найдена.")));
        return featureRepository.save(feature);
    }

    /**
     * Сохранение новой характеристики товара в таблицу item_feature.
     * Все характеристики товаров сохраняются только при сохранении самого товара.
     */
    @Transactional
    public ItemFeature createNewItemFeature(ItemFeature itemFeature, Item item, Feature feature) {
        itemFeature.setItem(item);
        itemFeature.setFeature(feature);
        return itemFeatureRepository.save(itemFeature);
    }

    @Transactional
    public Feature updateFeatureInfo(int featureId, Feature feature) {
        feature.setId(featureId);
        return featureRepository.save(feature);
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

    /**
     * Удаление характеристики категории и всех соответствующих
     * характеристик товаров.
     */
    @Transactional
    public void deleteFeature(int featureId) {
        itemFeatureRepository.deleteAllByFeatureId(featureId);
        featureRepository.deleteById(featureId);
    }
}
