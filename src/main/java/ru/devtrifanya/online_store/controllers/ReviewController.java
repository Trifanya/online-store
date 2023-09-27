package ru.devtrifanya.online_store.controllers;

import jakarta.validation.Valid;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.dto.ReviewDTO;
import ru.devtrifanya.online_store.models.Review;
import ru.devtrifanya.online_store.services.ReviewService;
import ru.devtrifanya.online_store.util.errorResponses.ErrorResponse;
import ru.devtrifanya.online_store.util.exceptions.review.InvalidReviewDataException;
import ru.devtrifanya.online_store.util.exceptions.review.NoReviewsException;
import ru.devtrifanya.online_store.util.exceptions.review.ReviewAlreadyPostedException;
import ru.devtrifanya.online_store.util.validators.ReviewValidator;

import java.util.List;

@RestController
@RequestMapping("/{userId}/reviews")
@Data
public class ReviewController {
    private final ReviewService reviewService;
    private final ModelMapper modelMapper;
    private final ReviewValidator reviewValidator;

    /** .../{userId}/reviews/{itemId} - просмотр отзывов о конкретном товаре, для пользователей и администратора;
     * Можно отсортировать отзывы по оценке: sortByRating = -1 - по возрастанию, ... = 1 - по убыванию, ... = 0 - сортировки нет. */
    @GetMapping("/{itemId}")
    public List<Review> showItemReviews(@PathVariable(name = "itemId") int itemId,
                                        @RequestParam(value = "sortByStars", defaultValue = "0") short sortByStars) {
        return reviewService.getItemReviews(itemId, sortByStars);
    }

    /** .../{userId}/reviews/{itemId}/new - добавление нового отзыва о конкретном товаре, только для пользователей; */
    @PostMapping("/{itemId}/new")
    public ResponseEntity<String> createReview(@RequestBody @Valid ReviewDTO reviewDTO,
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
            throw new InvalidReviewDataException(errorMessage.toString());
        }
        reviewService.save(convertToReview(reviewDTO), itemId, userId);
        return new ResponseEntity<>("Ваш отзыв успешно записан.", HttpStatus.CREATED);
    }

    /** .../{userId}/reviews/{reviewId} - удаление отзыва, только для администратора; */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable("reviewId") int reviewId) {
        reviewService.delete(reviewId);
        return new ResponseEntity<>("Отзыв успешно удален.", HttpStatus.OK);
    }


    public Review convertToReview(ReviewDTO reviewDTO) {
        return modelMapper.map(reviewDTO, Review.class);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        exception.printStackTrace();
        HttpStatus status = null;
        if (exception instanceof NoReviewsException) {
            status = HttpStatus.NOT_FOUND;
        } else if (exception instanceof ReviewAlreadyPostedException) {
            status = HttpStatus.ALREADY_REPORTED;
        } else if (exception instanceof InvalidReviewDataException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (exception instanceof MethodArgumentNotValidException) {
            status = HttpStatus.BAD_REQUEST;
        }
        ErrorResponse response = new ErrorResponse(exception.getMessage());
        return new ResponseEntity<>(response, status);
    }
}
