package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.models.Item;
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

    public List<Item> getFilteredItems(int categoryId, Map<String, String> filters) {
        List<Item> filteredItems = itemRepository.findAllByCategoryId(categoryId);

        for (Map.Entry<String, String> filter : filters.entrySet()) {
            String filterKey = filter.getKey();
            if (filterKey.contains("Range")) { // Границы диапазона
                filteredItems.retainAll(
                        this.getItemsWithFeatureInRange(filterKey, filter.getValue())
                );
            } else if (filterKey.contains("Values")) { // Множество значений
                filteredItems.retainAll(
                        this.getItemsWithFeatureFromSet(filterKey, filter.getValue())
                );
            } else if (filterKey.contains("Flag")) { // true или false
                filteredItems.retainAll(
                        this.getItemsWithFeatureFlag(filterKey, filter.getValue())
                );
            } else {
                throw new NotFoundException("Неизвестное название параметра запроса.");
            }
        }
        return filteredItems;
    }

    public List<Item> getItemsWithFeatureInRange(String filterName, String filterValue) {
        String[] filterValues = filterValue.split("-");

        double rangeStart = Double.parseDouble(filterValues[0]);
        double rangeEnd = Double.parseDouble(filterValues[1]);

        return itemRepository.findItemsWithFeatureInRange(
                filterName.substring(0, filterName.length() - "Range".length()),
                rangeStart,
                rangeEnd
        );
    }

    public List<Item> getItemsWithFeatureFromSet(String filterName, String filterValue) {
        Set<String> valuesSet = Arrays.stream(
                filterValue.split(",")
        ).collect(Collectors.toSet());

        return itemRepository.findItemsWithFeatureFromSet(
                filterName.substring(0, filterName.length() - "Values".length()),
                valuesSet
        );
    }

    public List<Item> getItemsWithFeatureFlag(String filterName, String filterValue) {
        boolean flag = Boolean.parseBoolean(filterValue);

        return itemRepository.findItemsWithFeatureFlag(
                filterName.substring(0, filterName.length() - "Flag".length()),
                flag
        );
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
