package ru.devtrifanya.online_store.services;

import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;

import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.Feature;
import ru.devtrifanya.online_store.models.ItemFeature;
import ru.devtrifanya.online_store.repositories.ItemFeatureRepository;

import java.util.List;

@Service
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

    /**
     * Получение характеристик товара по id товара.
     */
    public List<ItemFeature> getItemFeaturesByItemId(int itemId) {
        return itemFeatureRepository.findAllByItemId(itemId);
    }

    /**
     * Добавление новой характеристики товара или обновление существующей.
     */
    public ItemFeature createOrUpdateItemFeature(ItemFeature itemFeature, int itemId, int featureId) {
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
}
