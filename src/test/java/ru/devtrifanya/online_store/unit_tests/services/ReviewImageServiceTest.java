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
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
public class ReviewImageServiceTest {

    private static final int IMAGE_ID = 1;
    private static final int REVIEW_ID = 1;

    @Mock
    private ReviewService reviewServiceMock;
    @Mock
    private ReviewImageRepository reviewImageRepoMock;

    @InjectMocks
    private ReviewImageService testingService;


    @Test
    public void createNewReviewImage_shouldAssignIdAndReview() {
        // Given
        ReviewImage imageToSave = getImage(IMAGE_ID);
        mockGetReview();
        mockSave();

        // When
        ReviewImage resultImage = testingService.createNewReviewImage(imageToSave, REVIEW_ID);

        // Then
        Mockito.verify(reviewServiceMock).getReview(REVIEW_ID);
        Mockito.verify(reviewImageRepoMock).save(imageToSave);
        Assertions.assertNotNull(resultImage.getId());
        Assertions.assertEquals(getReview(REVIEW_ID), resultImage.getReview());
    }


    // Определение поведения mock-объектов.

    private void mockGetReview() {
        Mockito.doAnswer(invocationOnMock -> getReview(invocationOnMock.getArgument(0)))
                        .when(reviewServiceMock).getReview(anyInt());
    }

    private void mockSave() {
        Mockito.doAnswer(
                invocationOnMock -> {
                    ReviewImage image = invocationOnMock.getArgument(0);
                    image.setId(IMAGE_ID);
                    return image;
                }).when(reviewImageRepoMock).save(any(ReviewImage.class));
    }


    // Вспомогательные методы.

    private ReviewImage getImage(int imageId) {
        return new ReviewImage()
                .setId(imageId);
    }

    private Review getReview(int reviewId) {
        return new Review()
                .setId(reviewId);
    }
}
