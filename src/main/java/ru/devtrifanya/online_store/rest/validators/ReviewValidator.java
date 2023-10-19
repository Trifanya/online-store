package ru.devtrifanya.online_store.rest.validators;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import ru.devtrifanya.online_store.repositories.ReviewRepository;
import ru.devtrifanya.online_store.exceptions.NotFoundException;
import ru.devtrifanya.online_store.exceptions.AlreadyExistException;
import ru.devtrifanya.online_store.rest.dto.requests.AddReviewRequest;
import ru.devtrifanya.online_store.rest.dto.requests.DeleteReviewRequest;

@Component
@RequiredArgsConstructor
public class ReviewValidator {
    private final ReviewRepository reviewRepository;

    public void validateNewReview(AddReviewRequest request, int userId) {
        validateReviewIsNotExist(request.getItemId(), userId);
    }

    public void validateDeleteReview(DeleteReviewRequest request) {
        validateReviewIsExist(request.getReviewToDeleteId());
    }

    /**
     * Проверка наличия отзыва.
     * Если пользователь с указанным id уже оставлял отзыв о товаре с указанным id,
     * то выбрасывается исключение.
     */
    public void validateReviewIsNotExist(int itemId, int userId) {
        if (reviewRepository.findByItemIdAndUserId(itemId, userId).isPresent()) {
            throw new AlreadyExistException("Вы уже оставляли отзыв о данном товаре.");
        }
    }

    /**
     * Проверка наличия отзыва.
     * Если отзыв с указанным id не найден, то выбрасывается исключение.
     */
    public void validateReviewIsExist(int reviewId) {
        if (reviewRepository.findById(reviewId).isPresent()) {
            throw new NotFoundException("Отзыв с указанным id не найден.");
        }
    }
}
