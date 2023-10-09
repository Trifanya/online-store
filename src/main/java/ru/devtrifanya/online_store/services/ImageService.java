package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.models.Image;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.Review;
import ru.devtrifanya.online_store.repositories.ImageRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@Data
public class ImageService {
    private final ImageRepository imageRepository;

    public List<Image> getImagesByItemId(int itemId) {
        return imageRepository.findAllByItemId(itemId);
    }

    public List<Image> getImagesByReviewId(int reviewId) {
        return imageRepository.findAllByReviewId(reviewId);
    }

    public Image createNewImageForItem(Image imageToSave, Item item) {
        imageToSave.setItem(item);
        return imageRepository.save(imageToSave);
    }

    public Image createNewImageForReview(Image imageToSave, Review review) {
        imageToSave.setReview(review);
        return imageRepository.save(imageToSave);
    }

    public void deleteImage(int imageId) {
        imageRepository.deleteById(imageId);
    }
}
