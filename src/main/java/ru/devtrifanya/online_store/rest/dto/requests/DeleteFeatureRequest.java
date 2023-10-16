package ru.devtrifanya.online_store.rest.dto.requests;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class DeleteFeatureRequest {
    @Min(value = 1, message = "id характеристики должен быть не меньше 1")
    private int featureToDeleteId;
}
