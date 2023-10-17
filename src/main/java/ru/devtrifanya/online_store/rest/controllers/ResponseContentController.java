package ru.devtrifanya.online_store.rest.controllers;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.models.CartElement;
import ru.devtrifanya.online_store.services.*;
import ru.devtrifanya.online_store.rest.utils.MainClassConverter;
import ru.devtrifanya.online_store.rest.dto.responses.MainResponse;
import ru.devtrifanya.online_store.rest.dto.responses.CartInfoResponse;
import ru.devtrifanya.online_store.rest.dto.responses.ItemInfoResponse;
import ru.devtrifanya.online_store.rest.dto.responses.CategoryItemsResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class ResponseContentController {
    private final ItemService itemService;
    private final ReviewService reviewService;
    private final CategoryService categoryService;
    private final ItemFeatureService itemFeatureService;
    private final CartElementService cartElementService;

    private final MainClassConverter converter;

    @GetMapping("/catalog")
    public MainResponse getMainPage(@AuthenticationPrincipal User user) {
        MainResponse response = new MainResponse();

        response.setTopCategories(
                categoryService.getRootCategories().stream()
                        .map(category -> converter.convertToCategoryDTO(category))
                        .collect(Collectors.toList())
        );
        response.setCartSize(
                user == null ? 0 : cartElementService.getCartSizeByUserId(user.getId())
        );

        return response;
    }

    @GetMapping("/catalog/{categoryId}")
    public CategoryItemsResponse getItemsPage(@PathVariable("categoryId") int categoryId,
                                              @AuthenticationPrincipal User user,
                                              @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                                              @RequestParam(name = "itemsPerPage", defaultValue = "10") int itemsPerPage,
                                              @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
                                              @RequestParam(name = "sortDir", defaultValue = "ASC") String sortDir,
                                              @RequestParam Map<String, String> allParams) {
        CategoryItemsResponse categoryItemsResponse = new CategoryItemsResponse();

        Category currentCategory = categoryService.getCategory(categoryId);

        categoryItemsResponse.setCategoryFeatures(
                currentCategory.getFeatures().stream()
                        .map(feature -> converter.convertToFeatureDTO(feature))
                        .collect(Collectors.toList())
        );
        categoryItemsResponse.setCategoryItems(
                itemService.getFilteredItems(categoryId, allParams, pageNumber, itemsPerPage, sortBy, sortDir)
                        .stream()
                        .map(item -> converter.convertToItemDTO(item))
                        .collect(Collectors.toList())
        );
        categoryItemsResponse.setCurrentCategory(converter.convertToCategoryDTO(currentCategory));
        categoryItemsResponse.setTopCategories(
                categoryService.getRootCategories().stream()
                        .map(category -> converter.convertToCategoryDTO(category))
                        .collect(Collectors.toList())
        );
        categoryItemsResponse.setCartSize(
                user == null ? 0 : cartElementService.getCartSizeByUserId(user.getId())
        );

        return categoryItemsResponse;
    }

    @GetMapping("/catalog/{categoryId}/{itemId}")
    public ItemInfoResponse getItemPage(@PathVariable("itemId") int itemId,
                                        @AuthenticationPrincipal User user,
                                        @RequestParam(name = "sortByStars", defaultValue = "0") String sortByStars) {
        ItemInfoResponse itemInfoResponse = new ItemInfoResponse();
        itemInfoResponse.setItem(
                converter.convertToItemDTO(itemService.getItem(itemId))
        );
        itemInfoResponse.setItemFeatures(
                itemFeatureService.getItemFeaturesByItemId(itemId).stream()
                        .map(itemFeature -> converter.convertToItemFeatureDTO(itemFeature))
                        .collect(Collectors.toList())
        );
        itemInfoResponse.setItemReviews(
                reviewService.getReviewsByItemId(itemId, 0).stream()
                        .map(review -> converter.convertToReviewDTO(review))
                        .collect(Collectors.toList())
        );
        itemInfoResponse.setTopCategories(
                categoryService.getRootCategories().stream()
                        .map(category -> converter.convertToCategoryDTO(category))
                        .collect(Collectors.toList())
        );
        itemInfoResponse.setCartSize(
                user == null ? 0 : cartElementService.getCartSizeByUserId(user.getId())
        );
        return itemInfoResponse;
    }

    @GetMapping("/cart")
    public CartInfoResponse getCartPage(@AuthenticationPrincipal User user) {
        CartInfoResponse cartInfoResponse = new CartInfoResponse();

        List<CartElement> userCart = cartElementService.getCartElementsByUserId(user.getId());

        cartInfoResponse.setUserCart(
                userCart.stream()
                        .map(cartElement -> converter.convertToCartElementDTO(cartElement))
                        .collect(Collectors.toList())
        );
        cartInfoResponse.setItems(
                userCart.stream()
                        .map(cartElement -> converter.convertToItemDTO(cartElement.getItem()))
                        .collect(Collectors.toList())
        );
        cartInfoResponse.setTopCategories(
                categoryService.getRootCategories().stream()
                        .map(category -> converter.convertToCategoryDTO(category))
                        .collect(Collectors.toList())
        );

        return cartInfoResponse;
    }
}
