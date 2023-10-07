package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.models.Feature;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.ItemFeature;
import ru.devtrifanya.online_store.repositories.ItemRepository;
import ru.devtrifanya.online_store.util.exceptions.NotFoundException;

import java.util.List;

@Service
@Transactional(readOnly = true)
@Data
public class ItemService {
    private final ItemFeatureService itemFeatureService;

    private final ItemRepository itemRepository;


    /**
     * Получение товара по его id.
     * Метод получает на вход id товара, затем вызывает метд репозитория для поиска
     * товара по id и возвращает найденный товар.
     * Если товар не найден, то выбрасывается исключение.
     */
    public Item getItem(int itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Товар с таким названием не найден."));
    }

    public List<Item> getItemsByCategoryId(int categoryId) {
        return itemRepository.findAllByCategoryId(categoryId);
    }

    /**
     * Получение части списка товаров по id категории.
     * Метод получает на вход id категории, номер страницы, количество товаров на одной
     * странице и критерий сортировки, затем обращается к репозитория для получения
     * нужной страницы товаров с указанным id категории и возвращает список товаров,
     * соответствующий возвращенной репозиторием странице.
     */
    public List<Item> getItemsByCategoryId(int categoryId, int pageNum, int itemsPerPage, String sortBy) {
        return itemRepository.findAllByCategoryId(
                categoryId,
                PageRequest.of(pageNum, itemsPerPage, Sort.by(sortBy))
        ).getContent();
    }

    /**
     * Добавление нового товара.
     * Метод получает на вход товар, у которого проинициализированы все поля, кроме поля
     * rating и поля category, инициализирует rating нулевым значением, инициализирует
     * категорию, затем вызывает метод сервиса, сохраняющий каждую из характеристик
     * сохраняемого товара, далее вызывает метод репозитория для сохранения товара в БД
     * и возвращает сохраненный товар.
     */
    @Transactional
    public Item createNewItem(Item itemToSave, Category category) {
        itemToSave.setRating(0);
        itemToSave.setCategory(category);

        Item savedItem = itemRepository.save(itemToSave);

        for (int i = 0; i < itemToSave.getFeatures().size(); i++) {
            itemFeatureService.createNewItemFeature(
                    savedItem.getFeatures().get(i),
                    savedItem,
                    category.getFeatures().get(i)
            );
        }

        return savedItem;
    }

    /**
     * Обновление информации о товаре.
     * Метод получает на вход id товара, который нужно изменить, товар, у которого
     * проинициализированы все поля, кроме поля rating и поля category, инициализирует
     * rating, инициализирует категорию, затем вызывает метод сервиса, обновляющий каждую
     * из характеристик сохраняемого товара, далее вызывает метод репозитория для сохранения
     * товара в БД и возвращает обновленный товар.
     */
    @Transactional
    public Item updateItemInfo(int itemId, Item updatedItem, Category category) {
        Item oldItem = itemRepository.findById(itemId).get();

        updatedItem.setId(itemId);
        updatedItem.setRating(oldItem.getRating());
        updatedItem.setCategory(category);

        List<ItemFeature> oldFeatures = oldItem.getFeatures();
        List<ItemFeature> updatedFeatures = updatedItem.getFeatures();
        for (int i = 0; i < updatedItem.getFeatures().size(); i++) {
            itemFeatureService.updateItemFeatureInfo(
                    oldFeatures.get(i).getId(),
                    updatedFeatures.get(i),
                    updatedItem,
                    category.getFeatures().get(i)
            );
        }

        return itemRepository.save(updatedItem);
    }

    /**
     * Удаление товара.
     * Метод получает на вход id товара, который нужно удалить, затем вызывает метод
     * репозитория для удаления товара по id.
     */
    @Transactional
    public void deleteItem(int itemId) {
        itemRepository.deleteById(itemId);
    }
}
