package ru.devtrifanya.online_store.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.models.Review;
import ru.devtrifanya.online_store.models.ReviewImage;
import ru.devtrifanya.online_store.repositories.ReviewImageRepository;

@Service
@RequiredArgsConstructor
public class ReviewImageService {
    private final ReviewService reviewService;

    private final ReviewImageRepository reviewImageRepository;

    @Transactional
    public ReviewImage createNewReviewImage(ReviewImage imageToSave, int reviewId) {
        Review review = reviewService.getReview(reviewId);

        imageToSave.setId(0);
        imageToSave.setReview(review);

        return reviewImageRepository.save(imageToSave);
    }

}
