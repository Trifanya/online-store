package ru.devtrifanya.online_store.controllers;

import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.content.dto.ReviewDTO;
import ru.devtrifanya.online_store.models.Review;
import ru.devtrifanya.online_store.services.ReviewService;
import ru.devtrifanya.online_store.util.ErrorResponse;
import ru.devtrifanya.online_store.util.MainClassConverter;
import ru.devtrifanya.online_store.util.MainExceptionHandler;
import ru.devtrifanya.online_store.util.validators.ReviewValidator;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reviews/{itemId}")
@Data
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewValidator reviewValidator;
    private final MainExceptionHandler mainExceptionHandler;
    private final MainClassConverter converter;

    /**
     * Адрес: .../{itemId}/reviews/new/{userId}
     * Добавление нового отзыва о конкретном товаре, только для пользователей;
     */
    @PostMapping("/new/{userId}")
    public ResponseEntity<String> createNewReview(@RequestBody @Valid ReviewDTO reviewDTO,
                                                           @PathVariable("itemId") int itemId,
                                                           @PathVariable("userId") int userId,
                                                           BindingResult bindingResult) {
        reviewValidator.validate(itemId, userId);
        if (bindingResult.hasErrors()) {
            mainExceptionHandler.throwInvalidDataException(bindingResult);
        }
        Review createdReview = reviewService.createNewReview(
                converter.convertToReview(reviewDTO),
                itemId,
                userId
        );
        return ResponseEntity.ok("Ваш отзыв успешно записан. Спасибо за обратную связь!");
    }

    /**
     * Адрес: /{itemId}/reviews/delete/{reviewId}
     * Удаление отзыва, только для администратора;
     */
    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable("reviewId") int reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok("Отзыв успешно удален.");

    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        return mainExceptionHandler.handleException(exception);
    }
}
