package ru.devtrifanya.online_store.rest.dto.responses;

import lombok.Data;

import ru.devtrifanya.online_store.rest.dto.entities_dto.ItemDTO;
import ru.devtrifanya.online_store.rest.dto.entities_dto.FeatureDTO;
import ru.devtrifanya.online_store.rest.dto.entities_dto.CategoryDTO;

import java.util.List;

@Data
public class CategoryItemsResponse {

    private int cartSize;

    private CategoryDTO currentCategory;

    private List<ItemDTO> categoryItems;

    private List<CategoryDTO> topCategories;

    private List<FeatureDTO> categoryFeatures;

}
