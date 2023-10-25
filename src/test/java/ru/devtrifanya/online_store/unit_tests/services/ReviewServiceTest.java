package ru.devtrifanya.online_store.unit_tests.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.Review;
import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.services.ItemService;
import ru.devtrifanya.online_store.services.UserService;
import ru.devtrifanya.online_store.services.ReviewService;
import ru.devtrifanya.online_store.exceptions.NotFoundException;
import ru.devtrifanya.online_store.repositories.ReviewRepository;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
    @Mock
    private ItemService itemServiceMock;
    @Mock
    private UserService userServiceMock;
    @Mock
    private ReviewRepository reviewRepositoryMock;

    @InjectMocks
    private ReviewService testingService;

    private int itemId = 1;
    private int authorId = 1;

    private int reviewId = 1;
    private int savedReviewId = 2;

    private final String SORT_BY_STARS_ASC = "asc";
    private final String SORT_BY_STARS_DESC = "desc";
    private final String SORT_BY_STARS_NONE = "none";

    private Review foundReview = new Review(reviewId, 1, "c1");
    private Review reviewToSave = new Review(0, 5, "c2");

    private Item foundItem = new Item(itemId, "found", "m1", 100, 1, "d1", 1.0);
    private User foundAuthor = new User(authorId, "name", "surname", "email", "ROLE_USER");

    private List<Review> unsortedReviews = new ArrayList<>(
            List.of(
                    new Review(11, 1, "c11"),
                    new Review(12, 3, "c12"),
                    new Review(13, 2, "c13")
            ));
    private List<Review> sortedReviewsAsc = new ArrayList<>(
            List.of(
                    new Review(11, 1, "c11"),
                    new Review(12, 2, "c12"),
                    new Review(13, 3, "c13")
            ));
    private List<Review> sortedReviewsDesc = new ArrayList<>(
            List.of(
                    new Review(11, 3, "c11"),
                    new Review(12, 2, "c12"),
                    new Review(13, 1, "c13")
            ));

    @Test
    public void getReview_reviewIsExist_shouldReturnReview() {
        Mockito.when(reviewRepositoryMock.findById(reviewId))
                .thenReturn(Optional.of(foundReview));

        Review resultReview = testingService.getReview(reviewId);

        Mockito.verify(reviewRepositoryMock).findById(reviewId);
        Assertions.assertEquals(foundReview, resultReview);
    }

    @Test
    public void getReview_reviewIsNotExist_shouldThrowException() {
        Mockito.when(reviewRepositoryMock.findById(reviewId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                NotFoundException.class,
                () -> testingService.getReview(reviewId)
        );
        Mockito.verify(reviewRepositoryMock).findById(reviewId);
    }

    @Test
    public void getReviewsByItemId_sortByStarsIsNone() {
        Mockito.when(reviewRepositoryMock.findByItemId(itemId))
                .thenReturn(unsortedReviews);

        List<Review> resultReviews = testingService.getReviewsByItemId(itemId, SORT_BY_STARS_NONE);

        Mockito.verify(reviewRepositoryMock).findByItemId(itemId);
        Assertions.assertIterableEquals(unsortedReviews, resultReviews);
    }

    @Test
    public void getReviewsByItemId_sortByStarsIsAsc() {
        Mockito.when(reviewRepositoryMock.findByItemIdOrderByStarsAsc(itemId))
                .thenReturn(sortedReviewsAsc);

        List<Review> resultReviews = testingService.getReviewsByItemId(itemId, SORT_BY_STARS_ASC);

        Mockito.verify(reviewRepositoryMock).findByItemIdOrderByStarsAsc(itemId);
        Assertions.assertIterableEquals(sortedReviewsAsc, resultReviews);
    }

    @Test
    public void getReviewsByItemId_sortByStarsIsDesc() {
        Mockito.when(reviewRepositoryMock.findByItemIdOrderByStarsDesc(itemId))
                .thenReturn(sortedReviewsDesc);

        List<Review> resultReviews = testingService.getReviewsByItemId(itemId, SORT_BY_STARS_DESC);

        Mockito.verify(reviewRepositoryMock).findByItemIdOrderByStarsDesc(itemId);
        Assertions.assertIterableEquals(sortedReviewsDesc, resultReviews);
    }

    @Test
    public void createNewReview_shouldAssignId() {
        createNewReview_determineBehaviorOfMocks();

        Review resultReview = testingService.createNewReview(reviewToSave, itemId, authorId);

        Mockito.verify(reviewRepositoryMock).save(reviewToSave);
        Assertions.assertNotNull(resultReview.getId());
    }

    @Test
    public void createNewReview_shouldAssignItem() {
        createNewReview_determineBehaviorOfMocks();

        Review resultReview = testingService.createNewReview(reviewToSave, itemId, authorId);

        Mockito.verify(itemServiceMock).updateItemRating(itemId, reviewToSave.getStars());
        Assertions.assertEquals(foundItem, resultReview.getItem());
    }

    @Test
    public void createNewReview_shouldAssignUser() {
        createNewReview_determineBehaviorOfMocks();

        Review resultReview = testingService.createNewReview(reviewToSave, itemId, authorId);

        Mockito.verify(userServiceMock).getUser(authorId);
        Assertions.assertEquals(foundAuthor, resultReview.getUser());
    }

    @Test
    public void createNewReview_shouldAssignTimestamp() {
        createNewReview_determineBehaviorOfMocks();

        Review resultReview = testingService.createNewReview(reviewToSave, itemId, authorId);

        Assertions.assertNotNull(resultReview.getTimestamp());
    }

    @Test
    public void deleteReview_shouldInvokeRepoMethod() {
        testingService.deleteReview(reviewId);

        Mockito.verify(reviewRepositoryMock).deleteById(reviewId);
    }

    public void createNewReview_determineBehaviorOfMocks() {
        Mockito.when(itemServiceMock.updateItemRating(itemId, reviewToSave.getStars()))
                .thenReturn(foundItem);
        Mockito.when(userServiceMock.getUser(authorId))
                .thenReturn(foundAuthor);
        Mockito.doAnswer(
                invocationOnMock -> {
                    Review review = invocationOnMock.getArgument(0);
                    review.setId(savedReviewId);
                    return review;
                }).when(reviewRepositoryMock).save(any(Review.class));
    }
}
