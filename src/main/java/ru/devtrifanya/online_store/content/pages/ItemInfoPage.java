package ru.devtrifanya.online_store.content.pages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.ItemFeature;
import ru.devtrifanya.online_store.models.Review;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemInfoPage {

    private Item item;

    private List<ItemFeature> itemFeatures;

    private List<Review> itemReviews;

    private List<Category> topCategories;

    private int cartSize;

}
