package ru.devtrifanya.online_store.services.implementations;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
    private final ItemService itemService;
    private final FeatureService featureService;

    private final ItemFeatureRepository itemFeatureRepository;

    @Autowired
    public ItemFeatureService(@Lazy ItemService itemService, @Lazy FeatureService featureService,
                              ItemFeatureRepository itemFeatureRepository) {
        this.itemService = itemService;
        this.featureService = featureService;
        this.itemFeatureRepository = itemFeatureRepository;
    }

    public List<ItemFeature> getItemFeaturesByFeatureId(int featureId) {
        return itemFeatureRepository.findAllByFeatureId(featureId);
    }

    public List<ItemFeature> getItemFeaturesByItemId(int itemId) {
        return itemFeatureRepository.findAllByItemId(itemId);
    }

    /**
     * В метод передается характеристика товара, у которой проинициализировано только
     * значение, поэтому данный метод инициализирует у сохраняемой характеристики товара
     * поле item и поле feature и сохраняет эту характеристику в БД.
     */
    @Transactional
    //public ItemFeature createNewItemFeature(ItemFeature itemFeature, Item item, Feature feature) {
    public ItemFeature createNewItemFeature(ItemFeature itemFeature, int itemId, int featureId) {
        Item item = itemService.getItem(itemId);
        Feature feature = featureService.getFeature(featureId);

        itemFeature.setItem(item);
        itemFeature.setFeature(feature);

        String unit = feature.getUnit();
        String stringValue = itemFeature.getStringValue();
        if (unit != null) {
            itemFeature.setStringValue(stringValue + " " + feature.getUnit());
            itemFeature.setNumericValue(Double.parseDouble(stringValue));
        } else {
            itemFeature.setNumericValue((double) -1);
        }

        return itemFeatureRepository.save(itemFeature);
    }

    /**
     * В метод передается характеристика товара, у которой проинициализировано только
     * значение, поэтому данный метод инициализирует id, чтобы указать, характеристика с
     * каким id будет изменена, затем инициализирует поле item и поле feature и сохраняет
     * эту характеристику в БД.
     */
    @Transactional
    //public ItemFeature updateItemFeatureInfo(ItemFeature updatedItemFeature, Item item, Feature feature) {
    public ItemFeature updateItemFeatureInfo(ItemFeature updatedItemFeature, int itemId, int featureId) {
        Item item = itemService.getItem(itemId);
        Feature feature = featureService.getFeature(featureId);

        updatedItemFeature.setItem(item);
        updatedItemFeature.setFeature(feature);
        return itemFeatureRepository.save(updatedItemFeature);
    }
}
