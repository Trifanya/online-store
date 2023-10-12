package ru.devtrifanya.online_store.rest.dto.requests;

import lombok.Data;
import ru.devtrifanya.online_store.rest.dto.entities_dto.ReviewDTO;

@Data
public class NewReviewRequest {
    private ReviewDTO review;
    private int itemId;
}
