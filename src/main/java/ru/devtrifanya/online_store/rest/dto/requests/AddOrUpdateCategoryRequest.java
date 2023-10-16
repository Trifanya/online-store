package ru.devtrifanya.online_store.rest.dto.requests;

import jakarta.validation.Valid;
import lombok.Data;
import ru.devtrifanya.online_store.models.Feature;
import ru.devtrifanya.online_store.rest.dto.entities_dto.CategoryDTO;
import ru.devtrifanya.online_store.rest.dto.entities_dto.FeatureDTO;

import java.util.List;

@Data
public class AddOrUpdateCategoryRequest {
    private @Valid CategoryDTO category;

    private int parentCategoryId;

    private int[] existingFeaturesId;
    
    private List<@Valid FeatureDTO> newFeatures;

}
