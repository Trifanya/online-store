package ru.devtrifanya.online_store.controllers;

import jakarta.validation.Valid;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.dto.ReviewDTO;
import ru.devtrifanya.online_store.models.Review;
import ru.devtrifanya.online_store.services.ReviewService;
import ru.devtrifanya.online_store.util.ErrorResponse;
import ru.devtrifanya.online_store.util.MainClassConverter;
import ru.devtrifanya.online_store.util.MainExceptionHandler;
import ru.devtrifanya.online_store.util.exceptions.InvalidDataException;
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
     * Адрес: /{itemId}/reviews
     * Просмотр отзывов о конкретном товаре, для пользователей и администратора;
     * Можно отсортировать отзывы по оценке: sortByRating = -1 - по возрастанию, ... = 1 - по убыванию, ... = 0 - сортировки нет.
     */
    @GetMapping()
    public List<ReviewDTO> showItemReviews(@PathVariable(name = "itemId") int itemId,
                             @RequestParam(value = "sortByStars", defaultValue = "0") short sortByStars) {
        return reviewService.getAllItemReviews(itemId, sortByStars)
                .stream()
                .map(review -> converter.convertToReviewDTO(review))
                .collect(Collectors.toList());
    }

    /**
     * Адрес: .../{itemId}/reviews/new/{userId}
     * Добавление нового отзыва о конкретном товаре, только для пользователей;
     */
    @PostMapping("/new/{userId}")
    public ResponseEntity<String> addNewReview(@RequestBody @Valid ReviewDTO reviewDTO,
                                      @PathVariable("itemId") int itemId,
                                      @PathVariable("userId") int userId,
                                      BindingResult bindingResult) {
        reviewValidator.validate(itemId, userId);
        if (bindingResult.hasErrors()) {
            mainExceptionHandler.throwInvalidDataException(bindingResult);
        }
        reviewService.createNewReview(converter.convertToReview(reviewDTO), itemId, userId);

        return ResponseEntity.ok("Ваш отзыв принят. Спасибо за обратную связь!");
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
