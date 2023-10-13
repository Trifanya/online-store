package ru.devtrifanya.online_store.rest.controllers;

import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.models.Review;
import ru.devtrifanya.online_store.rest.dto.requests.NewReviewRequest;
import ru.devtrifanya.online_store.rest.dto.responses.ErrorResponse;
import ru.devtrifanya.online_store.services.ReviewService;
import ru.devtrifanya.online_store.rest.utils.MainClassConverter;
import ru.devtrifanya.online_store.rest.utils.MainExceptionHandler;
import ru.devtrifanya.online_store.rest.validators.ReviewValidator;

@RestController
@RequestMapping("/reviews")
@Data
public class ReviewController {
    private final ReviewService reviewService;

    private final ReviewValidator reviewValidator;

    private final MainExceptionHandler mainExceptionHandler;

    private final MainClassConverter converter;

    /**
     * Адрес: .../reviews/newReview
     * Добавление нового отзыва о конкретном товаре, только для пользователей;
     */
    @PostMapping("/newReview")
    public ResponseEntity<String> createNewReview(@RequestBody @Valid NewReviewRequest request,
                                                  @AuthenticationPrincipal User user) {
        reviewValidator.validate(request.getItemId(), user.getId());

        Review createdReview = reviewService.createNewReview(
                converter.convertToReview(request.getReview()),
                request.getItemId(),
                user.getId()
        );
        return ResponseEntity.ok("Ваш отзыв успешно записан. Спасибо за обратную связь!");
    }

    /**
     * Адрес: /reviews/delete/{reviewId}
     * Удаление отзыва, только для администратора;
     */
    @DeleteMapping("/deleteReview")
    public ResponseEntity<String> deleteReview(@RequestBody int reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok("Отзыв успешно удален.");

    }
}
