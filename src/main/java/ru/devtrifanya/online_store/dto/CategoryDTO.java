package ru.devtrifanya.online_store.dto;

import lombok.Data;
import ru.devtrifanya.online_store.models.Feature;
import ru.devtrifanya.online_store.models.Item;

import java.util.List;


@Data
public class CategoryDTO implements CatalogableDTO {
    private String name;

    private List<FeatureDTO> features;

    //private List<ItemDTO> items;
}
