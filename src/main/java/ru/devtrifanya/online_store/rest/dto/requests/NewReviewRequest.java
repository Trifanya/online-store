package ru.devtrifanya.online_store.rest.dto.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import ru.devtrifanya.online_store.rest.dto.entities_dto.ReviewDTO;

@Data
public class NewReviewRequest {
    private @Valid ReviewDTO review;

    private int itemId;
}
