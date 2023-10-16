package ru.devtrifanya.online_store.rest.dto.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.devtrifanya.online_store.rest.dto.entities_dto.ReviewDTO;

@Data
public class AddReviewRequest {
    @NotNull(message = "Необходимо указать содержимое отзыва.")
    private @Valid ReviewDTO review;

    @Min(value = 1, message = "id товара должен быть не меньше 1")
    private int itemId;
}
