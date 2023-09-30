package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.devtrifanya.online_store.repositories.ItemRepository;
import ru.devtrifanya.online_store.repositories.ReviewRepository;
import ru.devtrifanya.online_store.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
@Data
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    public void getAll_itemHasReviews_shouldReturnReviewsByItemId() {
        // given

        // when

        // then
    }

    @Test
    public void getAll_itemHasReviews_shouldReturnReviewsSortedByStars() {
        // given

        // when

        // then
    }

    @Test
    public void getAll_itemHasNoReviews_shouldThrowNotFoundException() {
        // given

        // when

        // then
    }

    @Test
    public void create_shouldSaveReview() {
        // given

        // when

        // then
    }

    @Test
    public void create_shouldRecalculateItemRating() {
        // given

        // when

        // then
    }

    @Test
    public void delete_shouldDeleteReviewById() {
        // given

        // when

        // then
    }

    @Test
    public void delete_shouldRecalculateItemRating() {
        // given

        // when

        // then
    }

}
