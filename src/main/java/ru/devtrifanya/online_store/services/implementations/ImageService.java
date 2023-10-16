package ru.devtrifanya.online_store.services.implementations;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.ItemImage;
import ru.devtrifanya.online_store.models.ReviewImage;
import ru.devtrifanya.online_store.repositories.ItemImageRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@Data
public class ImageService {
    private final ItemService itemService;
    private final ReviewService reviewService;

    private final ItemImageRepository itemImageRepository;

    @Autowired
    public ImageService(@Lazy ItemService itemService, @Lazy ReviewService reviewService,
                        ItemImageRepository itemImageRepository) {
        this.itemService = itemService;
        this.reviewService = reviewService;
        this.itemImageRepository = itemImageRepository;
    }

    @Transactional
    public ItemImage createNewImageIfNotExist(ItemImage itemImageToSave, int itemId) {
        Item item = itemService.getItem(itemId);
        ItemImage image = itemImageRepository.findByUrl(itemImageToSave.getUrl()).orElse(null);

        if (image != null) { // если изображение c указанным url есть в БД
            image.getItems().add(item);
            return itemImageRepository.save(image);
        } else { // если изображения с указанным url нет в БД
            itemImageToSave.setItems(new ArrayList<>());
            itemImageToSave.getItems().add(item);
            return itemImageRepository.save(itemImageToSave);
        }
    }

    @Transactional
    public void deleteImage(int imageId) {
        itemImageRepository.deleteById(imageId);
    }
}
