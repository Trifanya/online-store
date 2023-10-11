package ru.devtrifanya.online_store.content.pages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.devtrifanya.online_store.content.dto.CategoryDTO;
import ru.devtrifanya.online_store.content.dto.FeatureDTO;
import ru.devtrifanya.online_store.content.dto.ItemDTO;
import ru.devtrifanya.online_store.models.Category;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemsPage {

    private List<FeatureDTO> categoryFeatures;

    private List<ItemDTO> categoryItems;

    private CategoryDTO currentCategory;

    private List<CategoryDTO> topCategories;

    private int cartSize;
}
