package ru.devtrifanya.online_store.services;

import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;

import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.ItemImage;
import ru.devtrifanya.online_store.repositories.ItemImageRepository;

import java.util.List;

@Service
public class ImageService {
    private final ItemService itemService;

    private final ItemImageRepository itemImageRepository;

    @Autowired
    public ImageService(@Lazy ItemService itemService,
                        ItemImageRepository itemImageRepository) {
        this.itemService = itemService;
        this.itemImageRepository = itemImageRepository;
    }

    /**
     * Добавление нового изображения товара, если изображения с таким url еще нет в БД.
     */
    public ItemImage createNewImageIfNotExist(ItemImage itemImageToSave, int itemId) {
        Item item = itemService.getItem(itemId);
        ItemImage image = itemImageRepository.findByUrl(itemImageToSave.getUrl()).orElse(null);

        if (image != null) { // если изображение c указанным url уже есть в БД
            image.getItems().add(item);
            return itemImageRepository.save(image);
        } else { // если изображения с указанным url нет в БД
            itemImageToSave.setItems(List.of(item));
            return itemImageRepository.save(itemImageToSave);
        }
    }
}
