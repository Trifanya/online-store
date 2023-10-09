package ru.devtrifanya.online_store.content.pages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.devtrifanya.online_store.content.dto.CategoryDTO;
import ru.devtrifanya.online_store.content.dto.FeatureDTO;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.models.Item;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemsPage {

    private List<FeatureDTO> categoryFeatures;

    private List<Item> categoryItems;

    private Category currentCategory;

    private List<CategoryDTO> topCategories;

    private int cartSize;
}
