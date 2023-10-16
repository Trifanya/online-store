package ru.devtrifanya.online_store.services.implementations;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.repositories.CategoryRepository;
import ru.devtrifanya.online_store.repositories.ItemRepository;
import ru.devtrifanya.online_store.services.specifications.ItemSpecificationConstructor;
import ru.devtrifanya.online_store.exceptions.NotFoundException;

import java.util.*;

@Service
@Transactional(readOnly = true)
@Data
public class ItemService {
    private final ItemFeatureService itemFeatureService;

    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;

    private final ItemSpecificationConstructor specificationConstructor;

    @Autowired
    public ItemService(@Lazy ItemFeatureService itemFeatureService,
                       CategoryRepository categoryRepository, ItemRepository itemRepository, ItemSpecificationConstructor specificationConstructor) {
        this.itemFeatureService = itemFeatureService;
        this.categoryRepository = categoryRepository;
        this.itemRepository = itemRepository;
        this.specificationConstructor = specificationConstructor;
    }

    /**
     * Получение товара по его id.
     * Метод получает на вход id товара, затем вызывает метд репозитория для поиска
     * товара по id и возвращает найденный товар.
     * Если товар не найден, то выбрасывается исключение.
     */
    public Item getItem(int itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Товар с указанным id не найден."));
    }

    public List<Item> getFilteredItems(int categoryId,
                                       Map<String, String> filters,
                                       int pageNumber, int itemsPerPage, String sortBy, String sortDir) {
        for (String paramName : List.of("pageNumber", "itemsPerPage", "sortBy", "sortDir")) {
            filters.remove(paramName);
        }

        Specification<Item> itemSpecification = specificationConstructor.createItemSpecification(
                categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Категория с указанным id не найдена.")),
                filters
        );

        return itemRepository.findAll(
                itemSpecification,
                PageRequest.of(
                        pageNumber,
                        itemsPerPage,
                        Sort.by(Sort.Direction.valueOf(sortDir), sortBy)
                )
        ).getContent();
    }

    @Transactional
    public void buyItem(int itemId, int itemToBuyQuantity) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Товар с указанным id не найден."));
        item.setQuantity(item.getQuantity() - itemToBuyQuantity);
        itemRepository.save(item);
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

        itemToSave.setId(0);
        itemToSave.setRating(0);
        itemToSave.setCategory(itemCategory);

        return itemRepository.save(itemToSave);
    }

    @Transactional
    public Item updateItemInfo(Item updatedItem, int categoryId) {
        Item oldItem = itemRepository.findById(updatedItem.getId()).
                orElseThrow(() -> new NotFoundException("Товар с указанным id не найден."));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с указанным id не найдена."));

        updatedItem.setRating(oldItem.getRating());
        updatedItem.setCategory(category);

        return itemRepository.save(updatedItem);
    }

    /**
     * Перерассчет рейтинга товара при добавлении нового отзыва.
     */
    @Transactional
    public Item updateItemRating(int itemId, int newReviewRating) {
        Item itemToUpdateRating = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Товар с указанным id не найден."));
        int reviewsQuantity = itemToUpdateRating.getReviews().size();
        itemToUpdateRating.setRating(
                (itemToUpdateRating.getRating() * reviewsQuantity + newReviewRating) / (reviewsQuantity + 1)
        );

        return itemRepository.save(itemToUpdateRating);
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
