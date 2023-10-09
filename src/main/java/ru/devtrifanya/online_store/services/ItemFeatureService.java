package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.models.Feature;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.ItemFeature;
import ru.devtrifanya.online_store.repositories.ItemFeatureRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@Data
public class ItemFeatureService {
    private final ItemFeatureRepository itemFeatureRepository;

    public List<ItemFeature> getItemFeaturesByFeatureId(int featureId) {
        return itemFeatureRepository.findAllByFeatureId(featureId);
    }

    /**
     * В метод передается характеристика товара, у которой проинициализировано только
     * значение, поэтому данный метод инициализирует у сохраняемой характеристики товара
     * поле item и поле feature и сохраняет эту характеристику в БД.
     */
    @Transactional
    public ItemFeature createNewItemFeature(ItemFeature itemFeature, Item item, Feature feature) {
        itemFeature.setItem(item);
        itemFeature.setFeature(feature);
        return itemFeatureRepository.save(itemFeature);
    }

    /**
     * В метод передается характеристика товара, у которой проинициализировано только
     * значение, поэтому данный метод инициализирует id, чтобы указать, характеристика с
     * каким id будет изменена, затем инициализирует поле item и поле feature и сохраняет
     * эту характеристику в БД.
     */
    @Transactional
    public ItemFeature updateItemFeatureInfo(int itemFeatureId, ItemFeature updatedItemFeature, Item item, Feature feature) {
        updatedItemFeature.setId(itemFeatureId);
        updatedItemFeature.setItem(item);
        updatedItemFeature.setFeature(feature);
        return itemFeatureRepository.save(updatedItemFeature);
    }
}
