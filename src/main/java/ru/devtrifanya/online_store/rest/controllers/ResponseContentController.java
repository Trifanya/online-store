package ru.devtrifanya.online_store.rest.controllers;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import ru.devtrifanya.online_store.services.*;
import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.models.CartElement;
import ru.devtrifanya.online_store.rest.dto.responses.*;
import ru.devtrifanya.online_store.rest.utils.MainClassConverter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ResponseContentController {
    private final ItemService itemService;
    private final ReviewService reviewService;
    private final FeatureService featureService;
    private final CategoryService categoryService;
    private final ItemFeatureService itemFeatureService;
    private final CartElementService cartElementService;

    private final MainClassConverter converter;

    /**
     * Главная страница, для любого пользователя.
     */
    @GetMapping("/main")
    public MainResponse getMainPage(@AuthenticationPrincipal User user) {
        MainResponse response = new MainResponse();

        response.setTopCategories(
                categoryService.getRootCategories().stream()
                        .map(converter::convertToCategoryDTO)
                        .collect(Collectors.toList()));
        response.setCartSize(user == null ? 0 : cartElementService.getCartSizeByUserId(user.getId()));

        return response;
    }

    /**
     * Страница со списком всех характеристик категорий, только для администратора.
     */
    @GetMapping("/features")
    public AllFeaturesInfoResponse getAllFeatures() {
        AllFeaturesInfoResponse response = new AllFeaturesInfoResponse();

        response.setAllFeatures(
                featureService.getAllFeatures().stream()
                        .map(converter::convertToFeatureDTO)
                        .collect(Collectors.toList()));
        response.setRootCategories(
                categoryService.getRootCategories().stream()
                        .map(converter::convertToCategoryDTO)
                        .collect(Collectors.toList()));

        return response;
    }

    /**
     * Страница конечной категории, для любого пользователя.
     */
    @GetMapping("/categories/{categoryId}")
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
                        .map(converter::convertToFeatureDTO)
                        .collect(Collectors.toList()));
        categoryItemsResponse.setCategoryItems(
                itemService.getFilteredItems(categoryId, allParams, pageNumber, itemsPerPage, sortBy, sortDir)
                        .stream()
                        .map(converter::convertToItemDTO)
                        .collect(Collectors.toList()));
        categoryItemsResponse.setCurrentCategory(converter.convertToCategoryDTO(currentCategory));
        categoryItemsResponse.setTopCategories(
                categoryService.getRootCategories().stream()
                        .map(converter::convertToCategoryDTO)
                        .collect(Collectors.toList()));
        categoryItemsResponse.setCartSize(user == null ? 0 : cartElementService.getCartSizeByUserId(user.getId()));

        return categoryItemsResponse;
    }

    /**
     * Страница товара, для любого пользователя.
     */
    @GetMapping("/items/{itemId}")
    public ItemInfoResponse getItemPage(@PathVariable("itemId") int itemId,
                                        @AuthenticationPrincipal User user,
                                        @RequestParam(name = "sortByStars", defaultValue = "none") String sortByStars) {
        ItemInfoResponse itemInfoResponse = new ItemInfoResponse();
        itemInfoResponse.setItem(converter.convertToItemDTO(itemService.getItem(itemId)));
        itemInfoResponse.setItemFeatures(
                itemFeatureService.getItemFeaturesByItemId(itemId).stream()
                        .map(converter::convertToItemFeatureDTO)
                        .collect(Collectors.toList()));
        itemInfoResponse.setItemReviews(
                reviewService.getReviewsByItemId(itemId, sortByStars).stream()
                        .map(converter::convertToReviewDTO)
                        .collect(Collectors.toList()));
        itemInfoResponse.setTopCategories(
                categoryService.getRootCategories().stream()
                        .map(converter::convertToCategoryDTO)
                        .collect(Collectors.toList()));
        itemInfoResponse.setCartSize(user == null ? 0 : cartElementService.getCartSizeByUserId(user.getId()));
        return itemInfoResponse;
    }

    /**
     * Страница корзины, только для пользователя.
     */
    @GetMapping("/cart")
    public CartInfoResponse getCartPage(@AuthenticationPrincipal User user) {
        CartInfoResponse cartInfoResponse = new CartInfoResponse();

        List<CartElement> userCart = cartElementService.getCartElementsByUserId(user.getId());

        cartInfoResponse.setUserCart(
                userCart.stream()
                        .map(converter::convertToCartElementDTO)
                        .collect(Collectors.toList()));
        cartInfoResponse.setItems(
                userCart.stream()
                        .map(cartElement -> converter.convertToItemDTO(cartElement.getItem()))
                        .collect(Collectors.toList()));
        cartInfoResponse.setTopCategories(
                categoryService.getRootCategories().stream()
                        .map(converter::convertToCategoryDTO)
                        .collect(Collectors.toList()));

        return cartInfoResponse;
    }

    /**
     * Страница профиля, для пользователя и администратора.
     */
    @GetMapping("/profile")
    public UserProfileResponse getUserProfile(@AuthenticationPrincipal User currentUser) {
        UserProfileResponse response = new UserProfileResponse();

        response.setCartSize(0);
        response.setUser(converter.convertToUserDTO(currentUser));
        response.setRootCategories(
                categoryService.getRootCategories().stream()
                        .map(converter::convertToCategoryDTO)
                        .collect(Collectors.toList()));

        return response;
    }
}
