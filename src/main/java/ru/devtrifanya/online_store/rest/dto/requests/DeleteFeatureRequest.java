package ru.devtrifanya.online_store.rest.dto.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class DeleteFeatureRequest {
    @Positive(message = "id характеристики должен быть положительным")
    private int featureToDeleteId;
}
