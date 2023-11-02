package ru.devtrifanya.online_store.unit_tests.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.models.Review;
import ru.devtrifanya.online_store.services.ItemService;
import ru.devtrifanya.online_store.services.UserService;
import ru.devtrifanya.online_store.services.ReviewService;
import ru.devtrifanya.online_store.exceptions.NotFoundException;
import ru.devtrifanya.online_store.repositories.ReviewRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    private static final int ITEM_ID = 1;
    private static final int USER_ID = 1;
    private static final int REVIEW_ID = 1;
    private static final int SAVED_REVIEW_ID = 2;

    private static final int SOSO_RATING = 3;
    private static final int GOOD_RATING = 4;
    private static final int PERFECT_RATING = 5;

    private static final String SORT_BY_STARS_ASC = "asc";
    private static final String SORT_BY_STARS_DESC = "desc";
    private static final String SORT_BY_STARS_NONE = "none";

    @Mock
    private ItemService itemServiceMock;
    @Mock
    private UserService userServiceMock;
    @Mock
    private ReviewRepository reviewRepoMock;

    @InjectMocks
    private ReviewService testingService;

    @Test
    public void getReview_reviewIsExist_shouldReturnReview() {
        // Given
        mockFindById_exist();

        // When
        Review resultReview = testingService.getReview(REVIEW_ID);

        // Then
        Mockito.verify(reviewRepoMock).findById(REVIEW_ID);
        Assertions.assertEquals(getReview(REVIEW_ID, PERFECT_RATING), resultReview);
    }

    @Test
    public void getReview_reviewIsNotExist_shouldThrowException() {
        // Given
        mockFindById_notExist();

        // When // Then
        Assertions.assertThrows(NotFoundException.class, () -> testingService.getReview(REVIEW_ID));
    }

    @Test
    public void getReviewsByItemId_sortByStarsIsNone_shouldReturnReviews() {
        // Given
        mockFindByItemId();

        // When
        List<Review> resultReviews = testingService.getReviewsByItemId(ITEM_ID, SORT_BY_STARS_NONE);

        // Then
        Mockito.verify(reviewRepoMock).findByItemId(ITEM_ID);
        Assertions.assertIterableEquals(getUnsortedReviews(), resultReviews);
    }

    @Test
    public void getReviewsByItemId_sortByStarsIsAsc_shouldReturnReviewsAsc() {
        // Given
        mockFindByItemIdOrderByStarsAsc();

        // When
        List<Review> resultReviews = testingService.getReviewsByItemId(ITEM_ID, SORT_BY_STARS_ASC);

        // Then
        Mockito.verify(reviewRepoMock).findByItemIdOrderByStarsAsc(ITEM_ID);
        Assertions.assertIterableEquals(getReviewsSortedByStarsAsc(), resultReviews);
    }

    @Test
    public void getReviewsByItemId_sortByStarsIsDesc_shouldReturnReviewsDesc() {
        // Given
        mockFindByItemIdOrderByStarsDesc();

        // When
        List<Review> resultReviews = testingService.getReviewsByItemId(ITEM_ID, SORT_BY_STARS_DESC);

        // Then
        Mockito.verify(reviewRepoMock).findByItemIdOrderByStarsDesc(ITEM_ID);
        Assertions.assertIterableEquals(getReviewsSortedByStarsDesc(), resultReviews);
    }

    @Test
    public void createNewReview_shouldAssignIdAndUserAndItemAndTimestamp() {
        // Given
        Review reviewToSave = getReview(0, PERFECT_RATING);
        mockUpdateItemRating();
        mockGetUser();
        mockSaveNew();

        // When
        Review resultReview = testingService.createNewReview(reviewToSave, ITEM_ID, USER_ID);

        // Then
        Mockito.verify(itemServiceMock).updateItemRating(ITEM_ID, PERFECT_RATING);
        Mockito.verify(userServiceMock).getUser(USER_ID);
        Mockito.verify(reviewRepoMock).save(reviewToSave);
        Assertions.assertNotNull(resultReview.getId());
        Assertions.assertEquals(getItem(ITEM_ID), resultReview.getItem());
        Assertions.assertEquals(getUser(USER_ID), resultReview.getUser());
        Assertions.assertNotNull(resultReview.getTimestamp());

    }

    @Test
    public void deleteReview_shouldInvokeRepoMethod() {
        // When
        testingService.deleteReview(REVIEW_ID);

        // Then
        Mockito.verify(reviewRepoMock).deleteById(REVIEW_ID);
    }


    // Определение поведения mock-объектов.

    private void mockFindById_exist() {
        Mockito.doAnswer(invocationOnMock -> Optional.of(getReview(invocationOnMock.getArgument(0), PERFECT_RATING)))
                .when(reviewRepoMock).findById(anyInt());
    }

    private void mockFindById_notExist() {
        Mockito.when(reviewRepoMock.findById(anyInt()))
                .thenReturn(Optional.empty());
    }

    private void mockFindByItemId() {
        Mockito.when(reviewRepoMock.findByItemId(anyInt()))
                .thenReturn(getUnsortedReviews());
    }

    private void mockFindByItemIdOrderByStarsAsc() {
        Mockito.when(reviewRepoMock.findByItemIdOrderByStarsAsc(anyInt()))
                .thenReturn(getReviewsSortedByStarsAsc());
    }

    private void mockFindByItemIdOrderByStarsDesc() {
        Mockito.when(reviewRepoMock.findByItemIdOrderByStarsDesc(anyInt()))
                .thenReturn(getReviewsSortedByStarsDesc());
    }

    private void mockSaveNew() {
        Mockito.doAnswer(
                invocationOnMock -> {
                    Review review = invocationOnMock.getArgument(0);
                    review.setId(SAVED_REVIEW_ID);
                    return review;
                }).when(reviewRepoMock).save(any(Review.class));
    }

    private void mockGetUser() {
        Mockito.doAnswer(invocationOnMock -> getUser(invocationOnMock.getArgument(0)))
                .when(userServiceMock).getUser(anyInt());
    }

    private void mockUpdateItemRating() {
        Mockito.doAnswer(invocationOnMock -> getItem(invocationOnMock.getArgument(0)))
                .when(itemServiceMock).updateItemRating(anyInt(), anyInt());
    }


    // Вспомогательные методы.

    private Review getReview(int reviewId, int stars) {
        return new Review()
                .setId(reviewId)
                .setStars(stars);
    }

    private Review getReview(int stars, int userId, int itemId) {
        return new Review()
                .setId(REVIEW_ID)
                .setStars(stars)
                .setUser(getUser(userId))
                .setItem(getItem(itemId));
    }

    private User getUser(int userId) {
        return new User()
                .setId(userId);
    }

    private Item getItem(int itemId) {
        return new Item()
                .setId(itemId);
    }

    private List<Review> getUnsortedReviews() {
        return List.of(
                new Review().setStars(GOOD_RATING),
                new Review().setStars(SOSO_RATING),
                new Review().setStars(PERFECT_RATING)
        );
    }

    private List<Review> getReviewsSortedByStarsAsc() {
        return List.of(
                new Review().setStars(SOSO_RATING),
                new Review().setStars(GOOD_RATING),
                new Review().setStars(PERFECT_RATING)
        );
    }

    private List<Review> getReviewsSortedByStarsDesc() {
        return List.of(
                new Review().setStars(PERFECT_RATING),
                new Review().setStars(GOOD_RATING),
                new Review().setStars(SOSO_RATING)
        );
    }
}
