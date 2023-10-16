package ru.devtrifanya.online_store.rest.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.models.Review;
import ru.devtrifanya.online_store.rest.dto.requests.DeleteReviewRequest;
import ru.devtrifanya.online_store.rest.dto.requests.AddReviewRequest;
import ru.devtrifanya.online_store.services.implementations.ReviewImageService;
import ru.devtrifanya.online_store.services.implementations.ReviewService;
import ru.devtrifanya.online_store.rest.utils.MainClassConverter;
import ru.devtrifanya.online_store.rest.validators.ReviewValidator;

@RestController
@RequestMapping("/catalog/{categoryId}/{itemId}")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewImageService reviewImageService;

    private final ReviewValidator reviewValidator;

    private final MainClassConverter converter;

    /**
     * Адрес: .../reviews/newReview
     * Добавление нового отзыва о конкретном товаре, только для пользователей;
     */
    @PostMapping("/newReview")
    public ResponseEntity<String> createNewReview(@RequestBody @Valid AddReviewRequest request,
                                                  @AuthenticationPrincipal User user) {
        reviewValidator.validate(request, user.getId());

        // сохранение отзыва
        Review createdReview = reviewService.createNewReview(
                converter.convertToReview(request.getReview()),
                request.getItemId(),
                user.getId()
        );
        // сохранение изображений из отзыва
        request.getReview().getImages().stream()
                .forEach(reviewImage ->
                        reviewImageService.createNewReviewImage(
                                converter.convertToImage(reviewImage),
                                createdReview.getId()
                        ));
        return ResponseEntity.ok("Ваш отзыв успешно записан. Спасибо за обратную связь!");
    }

    /**
     * Адрес: /reviews/delete/{reviewId}
     * Удаление отзыва, только для администратора;
     */
    @DeleteMapping("/deleteReview")
    public ResponseEntity<String> deleteReview(@RequestBody @Valid DeleteReviewRequest request) {
        reviewValidator.validate(request);
        reviewService.deleteReview(request.getReviewToDeleteId());
        return ResponseEntity.ok("Отзыв успешно удален.");
    }
}
