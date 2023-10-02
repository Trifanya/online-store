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

@Service
@Transactional(readOnly = true)
@Data
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemFeatureRepository itemFeatureRepository;
    private final CategoryRepository categoryRepository;

    public Item getItem(int itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Товар с таким названием не найден."));
    }

    public List<Item> getItemsByCategory(int categoryId, int pageNum, int itemsPerPage, String sortBy) {
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
    public void createNewItem(Item item, int categoryId) {
        item.setCategory(categoryRepository.findById(categoryId).orElse(null));
        for (ItemFeature feature : item.getFeatures()) {
            itemFeatureRepository.save(feature);
        }

        itemRepository.save(item);
    }

    /**
     * Обновление информации о товаре и его характеристиках.
     */
    @Transactional
    public void updateItemInfo(int itemId, Item item) {
        item.setId(itemId);
        List<ItemFeature> oldFeatures = itemFeatureRepository.findAllByItemId(itemId);
        List<ItemFeature> updatedFeatures = item.getFeatures();
        for (int i = 0; i < oldFeatures.size(); i++) {
            updatedFeatures.get(i).setId(oldFeatures.get(i).getId());
            itemFeatureRepository.save(updatedFeatures.get(i));
        }
        itemRepository.save(item);
    }

    /**
     * Удаление товара из таблицы item. При удалении товара удаляются
     * все связанные с ним характеристики из таблицы item_feature.
     */
    @Transactional
    public void deleteItem(int itemId) {
        itemRepository.deleteById(itemId);
    }
}
