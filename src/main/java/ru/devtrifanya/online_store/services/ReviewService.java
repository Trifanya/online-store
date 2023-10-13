package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.Review;
import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.repositories.ItemRepository;
import ru.devtrifanya.online_store.repositories.ReviewRepository;
import ru.devtrifanya.online_store.repositories.UserRepository;
import ru.devtrifanya.online_store.exceptions.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@Data
public class ReviewService {
    private final ItemService itemService;

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ReviewRepository reviewRepository;

    /**
     * Получение списка всех отзывов о товаре.
     * Метод получает на вход id товара и параметр, указывающих порядок сортировки отзывов,
     * затем в зависимости от параметра сортировки вызывает метод репозитория для поиска
     * отзывов по id товара и возвращает найденный список отзывов.
     */
    public List<Review> getReviewsByItemId(int itemId, int sortByStars) {
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
    public Review createNewReview(Review reviewToSave, int itemId, int userId) {
        Item item = itemService.updateItemRating(itemId, reviewToSave.getStars());

        User user = userRepository.findById(userId)
                        .orElseThrow(() -> new NotFoundException("Пользователь с указанным id не найден."));

        reviewToSave.setItem(item);
        reviewToSave.setUser(user);
        reviewToSave.setTimestamp(LocalDateTime.now());

        return reviewRepository.save(reviewToSave);
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
