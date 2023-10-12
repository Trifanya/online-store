package ru.devtrifanya.online_store.rest.controllers;

import lombok.Data;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.rest.dto.responses.CartInfoResponse;
import ru.devtrifanya.online_store.rest.dto.responses.CategoryItemsResponse;
import ru.devtrifanya.online_store.rest.dto.responses.ItemInfoResponse;
import ru.devtrifanya.online_store.rest.dto.responses.MainResponse;
import ru.devtrifanya.online_store.services.*;
import ru.devtrifanya.online_store.util.MainClassConverter;
import ru.devtrifanya.online_store.util.MainExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping()
@Data
public class ResponseContentController {
    private final CategoryService categoryService;
    private final ItemService itemService;
    private final FeatureService featureService;
    private final ItemFeatureService itemFeatureService;
    private final CartElementService cartElementService;
    private final ReviewService reviewService;

    private final MainClassConverter converter;
    private final MainExceptionHandler exceptionHandler;

    @GetMapping("/catalog")
    public MainResponse getMainPage(@AuthenticationPrincipal User user) {
        MainResponse mainResponse = new MainResponse();
        mainResponse.setTopCategories(
                categoryService.getTopCategories()
                        .stream()
                        .map(category -> converter.convertToCategoryDTO(category))
                        .collect(Collectors.toList())
        );
        mainResponse.setCartSize(
                user == null ? 0 : cartElementService.getCartSizeByUserId(user.getId())
        );
        return mainResponse;
    }

    @GetMapping("/catalog/{categoryId}")
    public CategoryItemsResponse getItemsPage(@PathVariable("categoryId") int categoryId,
                                              @AuthenticationPrincipal User user,
                                              @RequestParam Map<String, String> allParams,
                                              @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                                              @RequestParam(name = "itemsPerPage", defaultValue = "10") int itemsPerPage,
                                              @RequestParam(name = "sortBy", defaultValue = "id") String sortBy) {
        CategoryItemsResponse categoryItemsResponse = new CategoryItemsResponse();

        Category currentCategory = categoryService.getCategory(categoryId);

        categoryItemsResponse.setCategoryFeatures(
                currentCategory.getFeatures()
                        .stream()
                        .map(feature -> converter.convertToFeatureDTO(feature))
                        .collect(Collectors.toList())
        );
        categoryItemsResponse.setCategoryItems(
                itemService.getFilteredItems(categoryId, allParams, pageNumber, itemsPerPage, sortBy)
                        .stream()
                        .map(item -> converter.convertToItemDTO(item))
                        .collect(Collectors.toList())
        );

        categoryItemsResponse.setCurrentCategory(converter.convertToCategoryDTO(currentCategory));
        categoryItemsResponse.setTopCategories(
                categoryService.getTopCategories()
                        .stream()
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
                itemFeatureService.getItemFeaturesByItemId(itemId)
                        .stream()
                        .map(itemFeature -> converter.convertToItemFeatureDTO(itemFeature))
                        .collect(Collectors.toList())
        );
        itemInfoResponse.setItemReviews(
                reviewService.getReviewsByItemId(itemId, 0)
                        .stream()
                        .map(review -> converter.convertToReviewDTO(review))
                        .collect(Collectors.toList())
        );
        itemInfoResponse.setTopCategories(
                categoryService.getTopCategories()
                        .stream()
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

        cartInfoResponse.setUserCart(
                cartElementService.getCartElementsByUserId(user.getId())
                        .stream()
                        .map(cartElement -> converter.convertToCartElementDTO(cartElement))
                        .collect(Collectors.toList())
        );
        cartInfoResponse.setTopCategories(
                categoryService.getTopCategories()
                        .stream()
                        .map(category -> converter.convertToCategoryDTO(category))
                        .collect(Collectors.toList())
        );

        return cartInfoResponse;
    }
}
