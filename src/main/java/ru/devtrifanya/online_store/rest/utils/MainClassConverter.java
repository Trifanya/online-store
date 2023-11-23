package ru.devtrifanya.online_store.rest.utils;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Component;

import ru.devtrifanya.online_store.models.*;
import ru.devtrifanya.online_store.rest.dto.entities_dto.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MainClassConverter {
    private final ModelMapper modelMapper;

    public Category convertToCategory(CategoryDTO dto) {
        return modelMapper.map(dto, Category.class);
    }

    public CategoryDTO convertToCategoryDTO(Category category) {
        CategoryDTO dto = modelMapper.map(category, CategoryDTO.class);
        dto.setChildren(
                category.getChildren().stream()
                        .map(child -> convertToCategoryDTO(child))
                        .collect(Collectors.toList())
        );
        return dto;
    }

    public Item convertToItem(ItemDTO dto) {
        return modelMapper.map(dto, Item.class);
    }

    public ItemDTO convertToItemDTO(Item item) {
        return modelMapper.map(item, ItemDTO.class);
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

    public UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    public ItemImage convertToImage(ItemImageDTO itemImageDTO) {
        return modelMapper.map(itemImageDTO, ItemImage.class);
    }

    public ItemImageDTO convertToImageDTO(ItemImage itemImage) {
        return modelMapper.map(itemImage, ItemImageDTO.class);
    }

    public ReviewImage convertToImage(ReviewImageDTO imageDTO) {
        return modelMapper.map(imageDTO, ReviewImage.class);
    }

    public ReviewImageDTO convertToImageDTO(ReviewImage image) {
        return modelMapper.map(image, ReviewImageDTO.class);
    }

    public Map<Integer, ItemFeature> convertToItemFeatureMap(Map<Integer, ItemFeatureDTO> dtoMap) {
        Map<Integer, ItemFeature> itemFeatures = new HashMap<>();
        for (Map.Entry<Integer, ItemFeatureDTO> itemFeature : dtoMap.entrySet()) {
            itemFeatures.put(
                    itemFeature.getKey(),
                    convertToItemFeature(itemFeature.getValue())
            );
        }
        return itemFeatures;
    }
}
