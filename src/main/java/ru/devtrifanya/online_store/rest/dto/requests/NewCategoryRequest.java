package ru.devtrifanya.online_store.rest.dto.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.devtrifanya.online_store.rest.dto.entities_dto.CategoryDTO;
import ru.devtrifanya.online_store.rest.dto.entities_dto.FeatureDTO;

import java.util.List;

@Data
public class NewCategoryRequest {
    private int parentCategoryId;
    @NotNull
    private CategoryDTO category;

    private List<FeatureDTO> features;

}
