package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.models.Review;
import ru.devtrifanya.online_store.repositories.ItemRepository;
import ru.devtrifanya.online_store.repositories.ReviewRepository;
import ru.devtrifanya.online_store.repositories.UserRepository;
import ru.devtrifanya.online_store.util.exceptions.review.NoReviewsException;

import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@Data
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public List<Review> getItemReviews(int itemId, short sortByStars) {
        List<Review> reviews = null;
        if (sortByStars == 1) {
            reviews = reviewRepository.findByItemIdOrderByStarsDesc(itemId);
        } else if (sortByStars == -1) {
            reviews = reviewRepository.findByItemIdOrderByStarsAsc(itemId);
        } else {
            reviews = reviewRepository.findByItemId(itemId);
        }
        if (reviews.size() == 0 || reviews == null) {
            throw new NoReviewsException();
        }
        return reviews;
    }

    @Transactional
    public void save(Review review, int itemId, int userId) {
        review.setItem(itemRepository.findById(itemId).get());
        review.setUser(userRepository.findById(userId).get());
        review.setTimestamp(LocalDateTime.now());
        reviewRepository.save(review);
    }

    @Transactional
    public void update(int id, Review review) {
        review.setId(id);
        reviewRepository.save(review);
    }

    @Transactional
    public void delete(int id) {
        reviewRepository.deleteById(id);
    }
}
