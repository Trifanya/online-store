package ru.devtrifanya.online_store.rest.dto.requests;

import jakarta.validation.Valid;
import lombok.Data;
import ru.devtrifanya.online_store.rest.dto.entities_dto.FeatureDTO;

@Data
public class NewFeatureRequest {
    private @Valid FeatureDTO feature;

    private int categoryId;
}
