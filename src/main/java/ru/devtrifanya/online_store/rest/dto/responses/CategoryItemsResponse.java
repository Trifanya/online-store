package ru.devtrifanya.online_store.rest.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.devtrifanya.online_store.rest.dto.entities_dto.CategoryDTO;
import ru.devtrifanya.online_store.rest.dto.entities_dto.FeatureDTO;
import ru.devtrifanya.online_store.rest.dto.entities_dto.ItemDTO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryItemsResponse {

    private List<FeatureDTO> categoryFeatures;

    private List<ItemDTO> categoryItems;

    private CategoryDTO currentCategory;

    private List<CategoryDTO> topCategories;

    private int cartSize;
}
