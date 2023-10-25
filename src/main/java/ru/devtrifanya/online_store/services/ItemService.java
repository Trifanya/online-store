package ru.devtrifanya.online_store.services;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;

import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.repositories.ItemRepository;
import ru.devtrifanya.online_store.exceptions.NotFoundException;
import ru.devtrifanya.online_store.services.specifications.ItemSpecificationConstructor;

import java.util.*;

@Service
public class ItemService {
    private final CategoryService categoryService;

    private final ItemRepository itemRepository;

    private final ItemSpecificationConstructor specificationConstructor;

    @Autowired
    public ItemService(@Lazy CategoryService categoryService,
                       ItemRepository itemRepository, ItemSpecificationConstructor specificationConstructor) {
        this.categoryService = categoryService;
        this.itemRepository = itemRepository;
        this.specificationConstructor = specificationConstructor;
    }

    /**
     * Получение товара по его id.
     */
    public Item getItem(int itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Товар с указанным id не найден."));
    }

    /**
     * Получение списка товаров согласно установленным фильтрам и параметрам сортировки.
     */
    public List<Item> getFilteredItems(int categoryId,
                                       Map<String, String> filters,
                                       int pageNumber, int itemsPerPage, String sortBy, String sortDir) {
        for (String paramName : List.of("pageNumber", "itemsPerPage", "sortBy", "sortDir")) {
            filters.remove(paramName);
        }

        Specification<Item> itemSpecification = specificationConstructor.createItemSpecification(
                categoryService.getCategory(categoryId),
                filters
        );

        return itemRepository.findAll(
                itemSpecification,
                PageRequest.of(
                        pageNumber,
                        itemsPerPage,
                        Sort.by(Sort.Direction.valueOf(sortDir), sortBy))
        ).getContent();
    }

    /**
     * Уменьшение количества товара при его покупке.
     */
    public Item reduceItemQuantity(int itemId, int itemToBuyQuantity) {
        Item item = getItem(itemId);

        item.setQuantity(item.getQuantity() - itemToBuyQuantity);

        return itemRepository.save(item);
    }

    /**
     * Добавление нового товара.
     */
    public Item createNewItem(Item itemToSave, int categoryId) {
        Category itemCategory = categoryService.getCategory(categoryId);

        itemToSave.setRating(0);
        itemToSave.setCategory(itemCategory);

        return itemRepository.save(itemToSave);
    }

    /**
     * Обновление информации о товаре.
     */
    public Item updateItemInfo(Item updatedItem, int categoryId) {
        Item oldItem = getItem(updatedItem.getId());
        Category category = categoryService.getCategory(categoryId);

        updatedItem.setRating(oldItem.getRating());
        updatedItem.setCategory(category);

        return itemRepository.save(updatedItem);
    }

    /**
     * Перерассчет рейтинга товара при добавлении нового отзыва.
     */
    public Item updateItemRating(int itemId, int newReviewRating) {
        Item itemToUpdate = getItem(itemId);

        double oldRating = itemToUpdate.getRating();
        int reviewsQuantity = itemToUpdate.getReviews().size();

        itemToUpdate.setRating(calculateNewRating(oldRating, reviewsQuantity, newReviewRating));

        return itemRepository.save(itemToUpdate);
    }

    /**
     * Вычисление нового рейтинга.
     */
    private double calculateNewRating(double oldRating, int reviewsQuantity, double newReviewRating) {
        return (oldRating * reviewsQuantity + newReviewRating) / (reviewsQuantity + 1);
    }

    /**
     * Удаление товара.
     */
    public void deleteItem(int itemId) {
        itemRepository.deleteById(itemId);
    }
}
