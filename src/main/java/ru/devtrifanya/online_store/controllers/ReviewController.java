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
import ru.devtrifanya.online_store.util.MainExceptionHandler;
import ru.devtrifanya.online_store.util.exceptions.InvalidDataException;
import ru.devtrifanya.online_store.util.validators.ReviewValidator;

import java.util.List;

@RestController
@RequestMapping("/reviews/{itemId}")
@Data
public class ReviewController {
    private final ReviewService reviewService;
    private final ModelMapper modelMapper;
    private final ReviewValidator reviewValidator;
    private final MainExceptionHandler mainExceptionHandler;

    /**
     * Адрес: /{itemId}/reviews
     * Просмотр отзывов о конкретном товаре, для пользователей и администратора;
     * Можно отсортировать отзывы по оценке: sortByRating = -1 - по возрастанию, ... = 1 - по убыванию, ... = 0 - сортировки нет.
     */
    @GetMapping()
    public List<Review> show(@PathVariable(name = "itemId") int itemId,
                             @RequestParam(value = "sortByStars", defaultValue = "0") short sortByStars) {
        return reviewService.getAll(itemId, sortByStars);
    }

    /**
     * Адрес: .../{itemId}/reviews/new/{userId}
     * Добавление нового отзыва о конкретном товаре, только для пользователей;
     */
    @PostMapping("/new/{userId}")
    public ResponseEntity<String> add(@RequestBody @Valid ReviewDTO reviewDTO,
                                      @PathVariable("itemId") int itemId,
                                      @PathVariable("userId") int userId,
                                      BindingResult bindingResult) {
        reviewValidator.validate(itemId, userId);

        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            StringBuilder errorMessage = new StringBuilder();
            for (FieldError error : errors) {
                errorMessage.append(error.getDefaultMessage() + "\n");
            }
            throw new InvalidDataException(errorMessage.toString());
        }
        reviewService.create(convertToReview(reviewDTO), itemId, userId);
        return ResponseEntity.ok("Ваш отзыв принят. Спасибо за обратную связь!");
    }

    /**
     * Адрес: /{itemId}/reviews/delete/{reviewId}
     * Удаление отзыва, только для администратора;
     */
    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<String> delete(@PathVariable("reviewId") int reviewId) {
        reviewService.delete(reviewId);
        return ResponseEntity.ok("Отзыв успешно удален.");

    }

    public Review convertToReview(ReviewDTO reviewDTO) {
        return modelMapper.map(reviewDTO, Review.class);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        return mainExceptionHandler.handleException(exception);
    }
}
