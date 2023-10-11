package ru.devtrifanya.online_store.content.pages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.devtrifanya.online_store.content.dto.CategoryDTO;
import ru.devtrifanya.online_store.content.dto.ItemDTO;
import ru.devtrifanya.online_store.content.dto.ItemFeatureDTO;
import ru.devtrifanya.online_store.content.dto.ReviewDTO;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.ItemFeature;
import ru.devtrifanya.online_store.models.Review;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemInfoPage {

    private ItemDTO item;

    private List<ItemFeatureDTO> itemFeatures;

    private List<ReviewDTO> itemReviews;

    private List<CategoryDTO> topCategories;

    private int cartSize;

}
