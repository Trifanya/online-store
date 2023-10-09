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
import ru.devtrifanya.online_store.repositories.CategoryRepository;
import ru.devtrifanya.online_store.repositories.ItemRepository;
import ru.devtrifanya.online_store.util.exceptions.NotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Data
public class ItemService {
    private final ItemFeatureService itemFeatureService;

    private final CategoryRepository categoryRepository;
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

    public List<Item> getItemsByCategoryIdWithFilters(Map<String, String> filters) {
        List<Item> items = null;
        for (Map.Entry<String, String> filter : filters.entrySet()) {
            String filterKey = filter.getKey();
            if (filterKey.contains("Range")) { // Границы диапазона
                String[] filterValue = filter.getValue().split("-");

                double rangeStart = Double.parseDouble(filterValue[0]);
                double rangeEnd = Double.parseDouble(filterValue[1]);

                //itemRepository...
            } else if (filterKey.contains("Values")) { // Множество значений
                Set<String> filterValues = Arrays.stream(
                        filter.getValue().split(",")
                        ).collect(Collectors.toSet());

                items = itemRepository.findItemsWithFeatureInRange(
                        filterKey.substring(0, filterKey.length() - "Values".length()),
                        filterValues
                );
            } else if (filterKey.contains("Flag")) { // true или false
                boolean flag = Boolean.parseBoolean(filter.getValue());

                //itemRepository...
            }
        }
        return items;
    }

    /**
     * Добавление нового товара.
     * Метод получает на вход товар, у которого проинициализированы все поля, кроме поля
     * rating и поля category, инициализирует rating нулевым значением, инициализирует
     * категорию полученным из сервиса объектом, затем для каждой характеристики товара
     * вызывает метод сервиса, сохраняющий эту характеристику, далее вызывает метод
     * репозитория для сохранения товара в БД и возвращает сохраненный товар.
     */
    @Transactional
    public Item createNewItem(Item itemToSave, int categoryId) {
        Category itemCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с указанным id не найдена."));

        itemToSave.setRating(0);
        itemToSave.setCategory(itemCategory);

        return itemRepository.save(itemToSave);
    }

    @Transactional
    public Item updateItemInfo(int itemId, Item updatedItem, int categoryId) {
        Item oldItem = itemRepository.findById(itemId).get();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с указанным id не найдена."));

        updatedItem.setId(itemId);
        updatedItem.setRating(oldItem.getRating());
        updatedItem.setCategory(category);

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
