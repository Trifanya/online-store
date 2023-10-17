package ru.devtrifanya.online_store.services;

import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;

import ru.devtrifanya.online_store.models.Review;
import ru.devtrifanya.online_store.models.ReviewImage;
import ru.devtrifanya.online_store.repositories.ReviewImageRepository;

@Service
public class ReviewImageService {
    private final ReviewService reviewService;

    private final ReviewImageRepository reviewImageRepository;

    @Autowired
    public ReviewImageService(@Lazy ReviewService reviewService,
                              ReviewImageRepository reviewImageRepository) {
        this.reviewService = reviewService;
        this.reviewImageRepository = reviewImageRepository;
    }

    /**
     * Добавление в изображения из отзыва.
     */
    public ReviewImage createNewReviewImage(ReviewImage imageToSave, int reviewId) {
        Review review = reviewService.getReview(reviewId);

        imageToSave.setReview(review);

        return reviewImageRepository.save(imageToSave);
    }

}
