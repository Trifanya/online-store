package ru.devtrifanya.online_store.services;

import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.models.Review;
import ru.devtrifanya.online_store.repositories.ReviewRepository;
import ru.devtrifanya.online_store.exceptions.NotFoundException;

import java.util.List;
import java.time.LocalDateTime;

@Service
public class ReviewService {
    private final ItemService itemService;
    private final UserService userService;

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(@Lazy ItemService itemService, @Lazy UserService userService,
                         ReviewRepository reviewRepository) {
        this.itemService = itemService;
        this.userService = userService;
        this.reviewRepository = reviewRepository;
    }

    /**
     * Получение отзыва по его id.
     */
    public Review getReview(int reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Отзыв с указанным id не найден."));
    }

    /**
     * Получение из списка всех отзывов о товаре.
     */
    public List<Review> getReviewsByItemId(int itemId, String sortByStars) {
        List<Review> reviews = null;
        if (sortByStars.equalsIgnoreCase("desc")) {
            reviews = reviewRepository.findByItemIdOrderByStarsDesc(itemId); // сначала высокие оценки
        } else if (sortByStars.equalsIgnoreCase("asc")) {
            reviews = reviewRepository.findByItemIdOrderByStarsAsc(itemId); // сначала низкие оценки
        } else {
            reviews = reviewRepository.findByItemId(itemId); // сортировка по оценке отсутствует
        }
        return reviews;
    }

    /**
     * Добавление в отзыва о товаре.
     */
    @Transactional
    public Review createNewReview(Review reviewToSave, int itemId, int authorId) {
        Item item = itemService.updateItemRating(itemId, reviewToSave.getStars());
        User reviewAuthor = userService.getUser(authorId);

        reviewToSave.setItem(item);
        reviewToSave.setUser(reviewAuthor);
        reviewToSave.setTimestamp(LocalDateTime.now());

        return reviewRepository.save(reviewToSave);
    }

    /**
     * Удаление из отзыва о товаре.
     */
    public void deleteReview(int reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}
