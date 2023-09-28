package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.ItemFeature;
import ru.devtrifanya.online_store.repositories.ItemFeatureRepository;
import ru.devtrifanya.online_store.repositories.ItemRepository;
import ru.devtrifanya.online_store.util.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Data
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemFeatureRepository itemFeatureRepository;

    public Item get(int itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isEmpty()) {
            throw new NotFoundException("Товар с таким названием не найден.");
        }
        return item.get();
    }

    public List<Item> getAll(int categoryId) {
        return itemRepository.findByCategoryId(categoryId);
    }

    @Transactional
    public void create(Item item, List<ItemFeature> features) {
        for (ItemFeature feature : features) {
            item.getFeatures().add(feature);
            itemFeatureRepository.save(feature);
        }
        itemRepository.save(item);
    }

    @Transactional
    public void update(int itemId, Item item, List<ItemFeature> features) {
        item.setId(itemId);
        itemRepository.save(item);
    }

    @Transactional
    public void delete(int itemId) {
        itemRepository.deleteById(itemId);
    }
}
