package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.ItemFeature;
import ru.devtrifanya.online_store.repositories.ItemFeatureRepository;
import ru.devtrifanya.online_store.repositories.ItemRepository;
import ru.devtrifanya.online_store.util.exceptions.item.ItemNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Data
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemFeatureRepository itemFeatureRepository;

    public Item findOne(int id) {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isEmpty()) {
            throw new ItemNotFoundException();
        }
        return item.get();
    }

    public List<Item> getItemsOfCategory(int categoryId) {
        return itemRepository.findByCategoryId(categoryId);
    }

    @Transactional
    public void save(Item item, List<ItemFeature> features) {
        for (ItemFeature feature : features) {
            item.getCharacteristics().add(feature);
            itemFeatureRepository.save(feature);
        }
        itemRepository.save(item);
    }

    @Transactional
    public void update(int id, Item item, List<ItemFeature> features) {
        item.setId(id);
        itemRepository.save(item);
    }

    @Transactional
    public void delete(int id) {
        itemRepository.deleteById(id);
    }
}
