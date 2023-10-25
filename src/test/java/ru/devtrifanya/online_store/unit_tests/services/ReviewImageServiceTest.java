package ru.devtrifanya.online_store.unit_tests.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.devtrifanya.online_store.models.Review;
import ru.devtrifanya.online_store.models.ReviewImage;
import ru.devtrifanya.online_store.services.ReviewService;
import ru.devtrifanya.online_store.services.ReviewImageService;
import ru.devtrifanya.online_store.repositories.ReviewImageRepository;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ReviewImageServiceTest {
    @Mock
    private ReviewService reviewServiceMock;
    @Mock
    private ReviewImageRepository reviewImageRepoMock;

    @InjectMocks
    private ReviewImageService testingService;

    private int reviewId = 1;
    private int savedImageId = 1;

    private Review foundReview = new Review(reviewId, 5, "c1");

    private ReviewImage imageToSave = new ReviewImage(0, "url1");

    @Test
    public void createNewReviewImage_shouldAssignId() {
        // Определение поведения mock-объектов
        createNewReviewImage_determineBehaviorOfMocks();

        // Выполнение тестируемого метода
        ReviewImage resultImage = testingService.createNewReviewImage(imageToSave, reviewId);
        // Проверка совпадения ожидаемого результата с реальным
        Mockito.verify(reviewImageRepoMock).save(imageToSave);
        Assertions.assertNotNull(resultImage.getId());
    }

    @Test
    public void createNewReviewImage_shouldAssignReview() {
        // Определение поведения mock-объектов
        createNewReviewImage_determineBehaviorOfMocks();

        // Выполнение тестируемого метода
        ReviewImage resultImage = testingService.createNewReviewImage(imageToSave, reviewId);
        // Проверка совпадения ожидаемого результата с реальным
        Mockito.verify(reviewServiceMock).getReview(reviewId);
        Assertions.assertEquals(foundReview, resultImage.getReview());
    }


    public void createNewReviewImage_determineBehaviorOfMocks() {
        Mockito.when(reviewServiceMock.getReview(reviewId))
                .thenReturn(foundReview);
        Mockito.doAnswer(invocationOnMock -> {
                    ReviewImage image = invocationOnMock.getArgument(0);
                    image.setId(savedImageId);
                    return image;
                }).when(reviewImageRepoMock).save(any(ReviewImage.class));
    }

}
