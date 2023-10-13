package ru.devtrifanya.online_store.rest.dto.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.devtrifanya.online_store.rest.dto.entities_dto.CategoryDTO;
import ru.devtrifanya.online_store.rest.dto.entities_dto.FeatureDTO;

import java.util.List;

@Data
public class NewCategoryRequest {
    private int parentCategoryId;

    private @Valid CategoryDTO category;

    private List<@Valid FeatureDTO> features;

}
