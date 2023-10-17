package ru.devtrifanya.online_store.rest.dto.responses;

import lombok.Data;

import ru.devtrifanya.online_store.rest.dto.entities_dto.ItemDTO;
import ru.devtrifanya.online_store.rest.dto.entities_dto.ReviewDTO;
import ru.devtrifanya.online_store.rest.dto.entities_dto.CategoryDTO;
import ru.devtrifanya.online_store.rest.dto.entities_dto.ItemFeatureDTO;

import java.util.List;

@Data
public class ItemInfoResponse {

    private int cartSize;

    private ItemDTO item;

    private List<ReviewDTO> itemReviews;

    private List<CategoryDTO> topCategories;

    private List<ItemFeatureDTO> itemFeatures;




}
