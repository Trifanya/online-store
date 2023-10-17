package ru.devtrifanya.online_store.rest.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.models.Review;
import ru.devtrifanya.online_store.rest.utils.MainClassConverter;
import ru.devtrifanya.online_store.rest.validators.ReviewValidator;
import ru.devtrifanya.online_store.rest.dto.requests.AddReviewRequest;
import ru.devtrifanya.online_store.rest.dto.requests.DeleteReviewRequest;
import ru.devtrifanya.online_store.services.ReviewService;
import ru.devtrifanya.online_store.services.ReviewImageService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/catalog/{categoryId}/{itemId}")
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewImageService reviewImageService;

    private final ReviewValidator reviewValidator;

    private final MainClassConverter converter;

    /**
     * Адрес: .../catalog/{categoryId}/{itemId}/newReview
     * Добавление нового отзыва о товаре, только для пользователя.
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
     * Адрес: .../catalog/{categoryId}/{itemId}/deleteReview
     * Удаление отзыва, только для администратора.
     */
    @DeleteMapping("/deleteReview")
    public ResponseEntity<String> deleteReview(@RequestBody @Valid DeleteReviewRequest request) {
        reviewValidator.validate(request);
        reviewService.deleteReview(request.getReviewToDeleteId());
        return ResponseEntity.ok("Отзыв успешно удален.");
    }
}
