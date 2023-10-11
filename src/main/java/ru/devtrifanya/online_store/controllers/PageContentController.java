package ru.devtrifanya.online_store.controllers;

import lombok.Data;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.content.dto.FeatureDTO;
import ru.devtrifanya.online_store.content.pages.CartPage;
import ru.devtrifanya.online_store.content.pages.ItemInfoPage;
import ru.devtrifanya.online_store.content.pages.ItemsPage;
import ru.devtrifanya.online_store.content.pages.MainPage;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.services.*;
import ru.devtrifanya.online_store.util.MainClassConverter;
import ru.devtrifanya.online_store.util.MainExceptionHandler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping()
@Data
public class PageContentController {
    private final CategoryService categoryService;
    private final ItemService itemService;
    private final FeatureService featureService;
    private final ItemFeatureService itemFeatureService;
    private final CartElementService cartElementService;
    private final ReviewService reviewService;

    private final MainClassConverter converter;
    private final MainExceptionHandler exceptionHandler;

    @GetMapping("/catalog")
    public MainPage getMainPage() {
        MainPage mainPage = new MainPage();
        mainPage.setTopCategories(
                categoryService.getTopCategories()
                        .stream()
                        .map(category -> converter.convertToCategoryDTO(category))
                        .collect(Collectors.toList())
        );
        mainPage.setCartSize(0);
        //catalogPage.setCartSize(cartElementService.getCartSizeByUserId(userId));
        return mainPage;
    }

    @GetMapping("/catalog/finalCategory/{categoryId}")
    public ItemsPage getItemsPage(@PathVariable("categoryId") int categoryId,
                                  @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                                  @RequestParam(name = "itemsPerPage", defaultValue = "10") int itemsPerPage,
                                  @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
                                  @RequestParam Map<String, String> allParams) {
        ItemsPage itemsPage = new ItemsPage();

        Category currentCategory = categoryService.getCategory(categoryId);

        itemsPage.setCategoryFeatures(
                currentCategory.getFeatures()
                        .stream()
                        .map(feature -> converter.convertToFeatureDTO(feature))
                        .collect(Collectors.toList())
        );
        /*itemsPage.setCategoryItems(
                itemService.getItemsByCategoryId(categoryId, pageNumber, itemsPerPage, sortBy)
                        .stream()
                        .map(item -> converter.convertToItemDTO(item))
                        .collect(Collectors.toList())
        );*/

        itemsPage.setCategoryItems(
                itemService.getFilteredItems(categoryId, allParams, pageNumber, itemsPerPage, sortBy)
                        .stream()
                        .map(item -> converter.convertToItemDTO(item))
                        .collect(Collectors.toList())
        );

        itemsPage.setCurrentCategory(converter.convertToCategoryDTO(currentCategory));
        itemsPage.setTopCategories(
                categoryService.getTopCategories()
                        .stream()
                        .map(category -> converter.convertToCategoryDTO(category))
                        .collect(Collectors.toList())
        );
        itemsPage.setCartSize(0);
        //itemsPage.setCartSize(cartElementService.getCartSizeByUserId());

        return itemsPage;
    }

    @GetMapping("/catalog/finalCategory/{itemId}/{categoryId}")
    public ItemInfoPage getItemPage(@PathVariable("itemId") int itemId,
                                    @RequestParam(name = "sortByStars", defaultValue = "0") String sortByStars) {
        ItemInfoPage itemInfoPage = new ItemInfoPage();
        itemInfoPage.setItem(
                converter.convertToItemDTO(itemService.getItem(itemId))
        );
        itemInfoPage.setItemFeatures(
                itemFeatureService.getItemFeaturesByItemId(itemId)
                        .stream()
                        .map(itemFeature -> converter.convertToItemFeatureDTO(itemFeature))
                        .collect(Collectors.toList())
        );
        itemInfoPage.setItemReviews(
                reviewService.getReviewsByItemId(itemId, 0)
                        .stream()
                        .map(review -> converter.convertToReviewDTO(review))
                        .collect(Collectors.toList())
        );
        itemInfoPage.setTopCategories(
                categoryService.getTopCategories()
                        .stream()
                        .map(category -> converter.convertToCategoryDTO(category))
                        .collect(Collectors.toList())
        );
        itemInfoPage.setCartSize(0);

        return itemInfoPage;
    }

    @GetMapping("/cart/{userId}")
    public CartPage getCartPage(@PathVariable("userId") int userId) {
        CartPage cartPage = new CartPage();

        cartPage.setUserCart(
                cartElementService.getCartElementsByUserId(userId)
                        .stream()
                        .map(cartElement -> converter.convertToCartElementDTO(cartElement))
                        .collect(Collectors.toList())
        );
        cartPage.setTopCategories(
                categoryService.getTopCategories()
                        .stream()
                        .map(category -> converter.convertToCategoryDTO(category))
                        .collect(Collectors.toList())
        );

        return cartPage;
    }
}
