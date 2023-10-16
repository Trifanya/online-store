package ru.devtrifanya.online_store.rest.dto.requests;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class DeleteReviewRequest {
    @Min(value = 1, message = "id отзыва должен быть не меньше 1")
    private int reviewToDeleteId;
}
