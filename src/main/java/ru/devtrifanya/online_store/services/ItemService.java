package ru.devtrifanya.online_store.services;

import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;

import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.models.ItemFeature;
import ru.devtrifanya.online_store.models.ItemImage;
import ru.devtrifanya.online_store.repositories.ItemRepository;
import ru.devtrifanya.online_store.exceptions.NotFoundException;
import ru.devtrifanya.online_store.services.specifications.ItemSpecificationConstructor;

import java.util.*;

@Service
public class ItemService {
    private final CategoryService categoryService;
    private final ItemFeatureService itemFeatureService;
    private final ImageService imageService;

    private final ItemRepository itemRepository;

    private final ItemSpecificationConstructor specificationConstructor;

    @Autowired
    public ItemService(@Lazy CategoryService categoryService,
                       @Lazy ItemFeatureService itemFeatureService,
                       @Lazy ImageService imageService,
                       ItemRepository itemRepository, ItemSpecificationConstructor specificationConstructor) {
        this.categoryService = categoryService;
        this.itemFeatureService = itemFeatureService;
        this.imageService = imageService;
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

        // Формирование объекта с фильтрами
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
     * Добавление нового товара или обновление информации о существующем.
     */
    @Transactional
    public Item createOrUpdateItem(Item item, int categoryId,
                                   Map<Integer, ItemFeature> itemFeatures,
                                   List<ItemImage> itemImages) {
        try {
            Item oldItem = getItem(item.getId());
            item.setRating(oldItem.getRating());
        } catch (NotFoundException exception) {
            item.setRating(0);
        }

        Category category = categoryService.getCategory(categoryId);
        item.setCategory(category);

        Item savedItem = itemRepository.save(item);

        // Обновление характеристик товара
        for (Map.Entry<Integer, ItemFeature> itemFeature : itemFeatures.entrySet()) {
            itemFeatureService.createOrUpdateItemFeature(
                    itemFeature.getValue(),
                    savedItem.getId(),
                    itemFeature.getKey()
            );
        }
        // Обновление изображений товара
        itemImages.forEach(
                image -> imageService.createNewImageIfNotExist(image, item.getId())
        );

        return savedItem;
    }

    /**
     * Уменьшение количества товара при его покупке.
     */
    public Item reduceItemQuantity(Item item, int itemToBuyQuantity) {
        try {
            item.setQuantity(item.getQuantity() - itemToBuyQuantity);
            return itemRepository.save(item);
        } catch (OptimisticLockException exception) {
            Item relevantItem = getItem(item.getId());
            return reduceItemQuantity(relevantItem, itemToBuyQuantity);
        }
    }

    /**
     * Перерассчет рейтинга товара при добавлении нового отзыва.
     */
    public Item updateItemRating(int itemId, int newReviewRating) {
        try {
            Item itemToUpdate = getItem(itemId);

            double oldRating = itemToUpdate.getRating();
            int reviewsQuantity = itemToUpdate.getReviews().size();

            itemToUpdate.setRating(calculateNewRating(oldRating, reviewsQuantity, newReviewRating));

            return itemRepository.save(itemToUpdate);

        } catch (OptimisticLockException exception) {
            return updateItemRating(itemId, newReviewRating);
        }
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
