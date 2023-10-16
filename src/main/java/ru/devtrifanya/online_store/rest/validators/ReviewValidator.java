package ru.devtrifanya.online_store.rest.validators;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.devtrifanya.online_store.exceptions.NotFoundException;
import ru.devtrifanya.online_store.repositories.ReviewRepository;
import ru.devtrifanya.online_store.exceptions.AlreadyExistException;
import ru.devtrifanya.online_store.rest.dto.requests.DeleteReviewRequest;
import ru.devtrifanya.online_store.rest.dto.requests.AddReviewRequest;

@Component
@Data
public class ReviewValidator {
    private final ReviewRepository reviewRepository;

    public void validate(AddReviewRequest request, int userId) {
        if (reviewRepository.findByItemIdAndUserId(request.getItemId(), userId).isPresent()) {
            throw new AlreadyExistException("Вы уже оставляли отзыв о данном товаре.");
        }
    }

    public void validate(DeleteReviewRequest request) {
        if (reviewRepository.findById(request.getReviewToDeleteId()).isPresent()) {
            throw new NotFoundException("Отзыв с указанным id не найден.");
        }
    }
}
