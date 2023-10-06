package ru.devtrifanya.online_store.util;

import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.devtrifanya.online_store.dto.*;
import ru.devtrifanya.online_store.models.*;

import java.util.stream.Collectors;

@Component
@Data
public class MainClassConverter {
    private final ModelMapper modelMapper;

    public Category convertToCategory(CategoryDTO dto) {
        Category category = modelMapper.map(dto, Category.class);

        category.setFeatures(dto.getFeatures()
                .stream()
                .map(featureDTO -> convertToFeature(featureDTO))
                .collect(Collectors.toList()));
        category.setItems(dto.getItems()
                .stream()
                .map(itemDTO -> convertToItem(itemDTO))
                .collect(Collectors.toList()));
        category.setRelationsWithChildren(dto.getRelationsWithChildren()
                .stream()
                .map(relationDTO -> convertToCategoryRelation(relationDTO))
                .collect(Collectors.toList()));

        return category;
    }

    public CategoryDTO convertToCategoryDTO(Category category) {
        CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);

        categoryDTO.setFeatures(category.getFeatures()
                .stream()
                .map(feature -> convertToFeatureDTO(feature))
                .collect(Collectors.toList()));
        categoryDTO.setItems(category.getItems()
                .stream()
                .map(item -> convertToItemDTO(item))
                .collect(Collectors.toList()));
        categoryDTO.setRelationsWithChildren(category.getRelationsWithChildren()
                .stream()
                .map(relation -> convertToCategoryRelationDTO(relation))
                .collect(Collectors.toList()));

        return categoryDTO;
    }

    public Item convertToItem(ItemDTO dto) {
        Item item = modelMapper.map(dto, Item.class);

        item.setFeatures(dto.getFeatures()
                .stream()
                .map(itemFeatureDTO -> convertToItemFeature(itemFeatureDTO))
                .collect(Collectors.toList()));

        return item;
    }

    public ItemDTO convertToItemDTO(Item item) {
        ItemDTO itemDTO = modelMapper.map(item, ItemDTO.class);

        itemDTO.setFeatures(item.getFeatures()
                .stream()
                .map(this::convertToItemFeatureDTO)
                .collect(Collectors.toList()));

        return itemDTO;
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
