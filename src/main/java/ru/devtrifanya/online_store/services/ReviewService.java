package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.Review;
import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.repositories.ReviewRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@Data
public class ReviewService {
    private final ReviewRepository reviewRepository;

    /**
     * Получение списка всех отзывов о товаре.
     * Метод получает на вход id товара и параметр, указывающих порядок сортировки отзывов,
     * затем в зависимости от параметра сортировки вызывает метод репозитория для поиска
     * отзывов по id товара и возвращает найденный список отзывов.
     */
    public List<Review> getReviewsByItemId(int itemId, short sortByStars) {
        List<Review> reviews = null;
        if (sortByStars == 1) {
            reviews = reviewRepository.findByItemIdOrderByStarsDesc(itemId);
        } else if (sortByStars == -1) {
            reviews = reviewRepository.findByItemIdOrderByStarsAsc(itemId);
        } else {
            reviews = reviewRepository.findByItemId(itemId);
        }
        return reviews;
    }

    /**
     * Добавления отзыва о товаре.
     * Метод получает на вход отзыв, у которого проинициализированы поля stars,
     * comment и image, инициализирует ему поля item и user, вызывает метод репозитория
     * для сохранения отзыва в БД и возвращает обновленный список отзывов.
     */
    @Transactional
    public List<Review> createNewReview(Review reviewToSave, Item item, User user) {
        reviewToSave.setItem(item);
        reviewToSave.setUser(user);
        reviewToSave.setTimestamp(LocalDateTime.now());
        reviewRepository.save(reviewToSave);
        return reviewRepository.findByItemId(item.getId());
    }

    /**
     * Удаление отзыва о товаре.
     * Метод получает на вход id отзыва, который нужно удалить и вызывает метод репозитория
     * для удаления отзыва с указанным id из БД.
     */
    @Transactional
    public void deleteReview(int reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}
