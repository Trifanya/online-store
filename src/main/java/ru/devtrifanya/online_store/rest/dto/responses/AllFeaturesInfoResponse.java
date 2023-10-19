package ru.devtrifanya.online_store.rest.dto.responses;

import lombok.Data;
import ru.devtrifanya.online_store.rest.dto.entities_dto.CategoryDTO;
import ru.devtrifanya.online_store.rest.dto.entities_dto.FeatureDTO;

import java.util.List;

@Data
public class AllFeaturesInfoResponse {
    private List<FeatureDTO> allFeatures;
    private List<CategoryDTO> rootCategories;
}
