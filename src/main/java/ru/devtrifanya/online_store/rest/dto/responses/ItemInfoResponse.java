package ru.devtrifanya.online_store.rest.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.devtrifanya.online_store.rest.dto.entities_dto.CategoryDTO;
import ru.devtrifanya.online_store.rest.dto.entities_dto.ItemDTO;
import ru.devtrifanya.online_store.rest.dto.entities_dto.ItemFeatureDTO;
import ru.devtrifanya.online_store.rest.dto.entities_dto.ReviewDTO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemInfoResponse {

    private ItemDTO item;

    private List<ItemFeatureDTO> itemFeatures;

    private List<ReviewDTO> itemReviews;

    private List<CategoryDTO> topCategories;

    private int cartSize;

}
