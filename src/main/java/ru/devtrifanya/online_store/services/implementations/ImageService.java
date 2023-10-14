package ru.devtrifanya.online_store.services.implementations;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
    private final ItemService itemService;
    private final ReviewService reviewService;

    private final ImageRepository imageRepository;

    @Autowired
    public ImageService(@Lazy ItemService itemService, @Lazy ReviewService reviewService,
                        ImageRepository imageRepository) {
        this.itemService = itemService;
        this.reviewService = reviewService;
        this.imageRepository = imageRepository;
    }

    public List<Image> getImagesByItemId(int itemId) {
        return imageRepository.findAllByItemId(itemId);
    }

    public List<Image> getImagesByReviewId(int reviewId) {
        return imageRepository.findAllByReviewId(reviewId);
    }

    //public Image createNewImageForItem(Image imageToSave, Item item) {
    public Image createNewImageForItem(Image imageToSave, int itemId) {
        Item item = itemService.getItem(itemId);
        imageToSave.setItem(item);
        return imageRepository.save(imageToSave);
    }

    //public Image createNewImageForReview(Image imageToSave, Review review) {
    public Image createNewImageForReview(Image imageToSave, int reviewId) {
        Review review = reviewService.getReview(reviewId);
        imageToSave.setReview(review);
        return imageRepository.save(imageToSave);
    }

    public void deleteImage(int imageId) {
        imageRepository.deleteById(imageId);
    }
}
