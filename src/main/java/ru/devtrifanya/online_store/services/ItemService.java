package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.ItemFeature;
import ru.devtrifanya.online_store.repositories.CategoryRepository;
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
    private final CategoryRepository categoryRepository;

    public Item get(int itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isEmpty()) {
            throw new NotFoundException("Товар с таким названием не найден.");
        }
        return item.get();
    }

    public List<Item> getAll(int categoryId, int pageNum, int itemsPerPage, String sortBy) {
        return itemRepository.findAllByCategoryId(
                categoryId,
                PageRequest.of(pageNum, itemsPerPage, Sort.by(sortBy))
        ).getContent();
    }

    /**
     * Добавление нового товара.
     * При этом товару назначается категория, в которой он был добавлен.
     * При добавлении товара в таблицу ItemFeatures добавляются
     * все характеристики нового товара.
     */
    @Transactional
    public void create(Item item, int categoryId, List<ItemFeature> features) {
        item.setCategory(categoryRepository.findById(categoryId).orElse(null));
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
        /** Возможно, нужно настроить каскадирование.*/
        itemRepository.deleteById(itemId);
    }
}
