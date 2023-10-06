package ru.devtrifanya.online_store.dto;

import lombok.Data;
import ru.devtrifanya.online_store.models.ItemFeature;

import java.util.List;

@Data
public class FeatureDTO {
    private String name;
    private List<ItemFeature> itemFeatures;
}
