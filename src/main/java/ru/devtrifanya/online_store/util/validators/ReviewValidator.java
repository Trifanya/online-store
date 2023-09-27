package ru.devtrifanya.online_store.util.validators;

import jakarta.persistence.Column;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.devtrifanya.online_store.models.Review;
import ru.devtrifanya.online_store.repositories.ReviewRepository;
import ru.devtrifanya.online_store.util.exceptions.review.ReviewAlreadyPostedException;

@Component
@Data
public class ReviewValidator {
    private final ReviewRepository reviewRepository;

    public void validate(int itemId, int userId) {
        if (reviewRepository.findByItemIdAndUserId(itemId, userId).isPresent()) {
            throw new ReviewAlreadyPostedException();
        }
    }
}
