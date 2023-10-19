package ru.devtrifanya.online_store.rest.dto.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.devtrifanya.online_store.rest.dto.entities_dto.CategoryDTO;
import ru.devtrifanya.online_store.rest.dto.entities_dto.FeatureDTO;

import java.util.List;

@Data
public class AddOrUpdateCategoryRequest {

    private int newParentId;

    private int prevParentId;

    private int[] existingFeaturesId;

    private @Valid CategoryDTO category;

    @NotNull(message = "Список новых характеристик не должен быть null")
    private List<@Valid FeatureDTO> newFeatures;

}
