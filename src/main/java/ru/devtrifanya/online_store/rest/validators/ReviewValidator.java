package ru.devtrifanya.online_store.rest.validators;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.devtrifanya.online_store.repositories.ReviewRepository;
import ru.devtrifanya.online_store.util.exceptions.AlreadyExistException;

@Component
@Data
public class ReviewValidator {
    private final ReviewRepository reviewRepository;

    public void validate(int itemId, int userId) {
        if (reviewRepository.findByItemIdAndUserId(itemId, userId).isPresent()) {
            throw new AlreadyExistException("Вы уже оставляли отзыв о данном товаре.");
        }
    }
}
