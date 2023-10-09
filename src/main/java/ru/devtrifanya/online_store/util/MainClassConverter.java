package ru.devtrifanya.online_store.util;

import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.devtrifanya.online_store.content.dto.*;
import ru.devtrifanya.online_store.models.*;

@Component
@Data
public class MainClassConverter {
    private final ModelMapper modelMapper;

    public Category convertToCategory(CategoryDTO dto) {
        return modelMapper.map(dto, Category.class);
    }

    public CategoryDTO convertToCategoryDTO(Category category) {
        return modelMapper.map(category, CategoryDTO.class);
    }

    public Item convertToItem(ItemDTO dto) {
        return modelMapper.map(dto, Item.class);
    }

    public ItemDTO convertToItemDTO(Item item) {
        return modelMapper.map(item, ItemDTO.class);
    }

    public CategoryRelation convertToCategoryRelation(CategoryRelationDTO dto) {
        return modelMapper.map(dto, CategoryRelation.class);
    }

    public CategoryRelationDTO convertToCategoryRelationDTO(CategoryRelation relation) {
        return modelMapper.map(relation, CategoryRelationDTO.class);
    }

    public Feature convertToFeature(FeatureDTO dto) {
        return modelMapper.map(dto, Feature.class);
    }

    public FeatureDTO convertToFeatureDTO(Feature feature) {
        return modelMapper.map(feature, FeatureDTO.class);
    }

    public ItemFeature convertToItemFeature(ItemFeatureDTO dto) {
        return modelMapper.map(dto, ItemFeature.class);
    }

    public ItemFeatureDTO convertToItemFeatureDTO(ItemFeature itemFeature) {
        return modelMapper.map(itemFeature, ItemFeatureDTO.class);
    }

    public Review convertToReview(ReviewDTO dto) {
        return modelMapper.map(dto, Review.class);
    }

    public ReviewDTO convertToReviewDTO(Review review) {
        return modelMapper.map(review, ReviewDTO.class);
    }

    public CartElement convertToCartElement(CartElementDTO dto) {
        return modelMapper.map(dto, CartElement.class);
    }

    public CartElementDTO convertToCartElementDTO(CartElement cartElement) {
        return modelMapper.map(cartElement, CartElementDTO.class);
    }

    public User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

}
