package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.models.Feature;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.ItemFeature;
import ru.devtrifanya.online_store.repositories.CategoryRepository;
import ru.devtrifanya.online_store.repositories.FeatureRepository;
import ru.devtrifanya.online_store.repositories.ItemFeatureRepository;
import ru.devtrifanya.online_store.repositories.ItemRepository;
import ru.devtrifanya.online_store.util.exceptions.NotFoundException;

import java.util.List;

@Service
@Transactional(readOnly = true)
@Data
public class ItemService {
    private final FeatureService featureService;

    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;
    private final FeatureRepository featureRepository;
    private final ItemFeatureRepository itemFeatureRepository;


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
     * Добавление нового товара. При этом товару назначается категория, в которой он был добавлен.
     * При добавлении товара в таблицу ItemFeatures сохраняются все характеристики нового товара.
     * Характеристики товара обязательно должны сохраняться после самого товара, т.к. у товара до
     * сохранения id = 0, а после сохранения id становится равным какому-то значению,
     * сгенерированному автоматически. Если же попытаться сохранить характеристику товара, передав
     * в метод сохранения характеристики товар с id = 0, то будет ошибка, т.к. поле item_id
     * в таблице item_feature не должно быть нулевым.
     */
    @Transactional
    public Item createNewItem(Item itemToSave, int categoryId) {
        itemToSave.setCategory(categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория товара не найдена.")));
        itemRepository.save(itemToSave);

        /** Получение списка характеристик, которые есть у категории сохраняемого товара. */
        List<Feature> categoryFeatures = featureRepository.findAllByCategoryId(categoryId);

        /** Каждая из характеристик сохраняемого товара сохраняется в таблицу ItemFeatures */
        featureService.createSeveralNewItemFeatures(itemToSave, categoryFeatures);

        return itemToSave;
    }

    /**
     * Обновление информации о товаре и его характеристиках.
     */
    @Transactional
    public Item updateItemInfo(int itemId, Item updatedItem, int categoryId) {
        updatedItem.setId(itemId);
        updatedItem.setCategory(categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория товара не найдена.")));
        itemRepository.save(updatedItem);

        /** Получение списка характеристик, которые есть у категории сохраняемого товара. */
        List<Feature> categoryFeatures = featureRepository.findAllByCategoryId(categoryId);

        /** Получение списка характеристик, которые есть у сохраняемого товара. */
        List<ItemFeature> updatedItemFeatures = updatedItem.getFeatures();

        /** Апдейт каждой из характеристик сохраняемого товара */
        featureService.updateSeveralItemFeaturesInfo(updatedItem, categoryFeatures);

        return updatedItem;
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
