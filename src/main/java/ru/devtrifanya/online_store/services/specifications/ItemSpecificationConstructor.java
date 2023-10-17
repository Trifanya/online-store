package ru.devtrifanya.online_store.services.specifications;

import jakarta.persistence.criteria.*;

import org.springframework.stereotype.Component;
import org.springframework.data.jpa.domain.Specification;

import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.Feature;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.models.ItemFeature;
import ru.devtrifanya.online_store.exceptions.NotFoundException;

import java.util.Map;
import java.util.Set;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class ItemSpecificationConstructor {

    public Specification<Item> createItemSpecification(Category category, Map<String, String> filters) {
        Specification<Item> itemSpecification = itemQuantityIsNotNull().and(itemCategoryIs(category));

        for (Map.Entry<String, String> filter : filters.entrySet()) {
            String filterName = filter.getKey();
            String filterValue = filter.getValue();

            if (filterName.equals("price")) { // Диапазон цены товара
                String[] filterValues = filterValue.split("-");
                double rangeStart = Double.parseDouble(filterValues[0]);
                double rangeEnd = Double.parseDouble(filterValues[1]);
                itemSpecification = itemSpecification.and(itemPriceWithinRange(rangeStart, rangeEnd));
            } else if (filterName.equals("manufacturer")) { // Множество производителей товара
                Set<String> manufacturers = Arrays.stream(
                        filterValue.split(",")
                ).collect(Collectors.toSet());
                itemSpecification = itemSpecification.and(itemManufacturerInSet(manufacturers));
            } else if (filterName.equals("rating")) { // Минимальный рейтинг товара
                double minRating = Double.parseDouble(filterValue);
                itemSpecification = itemSpecification.and(itemRatingIsMoreThan(minRating));
            } else if (filterName.contains("Range")) { // Диапазон значений характеристики товара
                String requestName = filterName.substring(0, filterName.length() - "Range".length());
                String[] filterValues = filterValue.split("-");
                double rangeStart = Double.parseDouble(filterValues[0]);
                double rangeEnd = Double.parseDouble(filterValues[1]);
                itemSpecification = itemSpecification.and(itemFeatureWithinRange(requestName, rangeStart, rangeEnd));
            } else if (filterName.contains("Values")) { // Множество значений характеристики товара
                String requestName = filterName.substring(0, filterName.length() - "Values".length());
                Set<String> valuesSet = Arrays.stream(
                        filterValue.split(",")
                ).collect(Collectors.toSet());
                itemSpecification = itemSpecification.and(itemFeatureInSet(requestName, valuesSet));
            } else if (filterName.contains("Flag")) { // true или false
                String requestName = filterName.substring(0, filterName.length() - "Flag".length());
                itemSpecification = itemSpecification.and(itemFeatureFlagIs(requestName, Boolean.parseBoolean(filter.getValue())));
            } else {
                throw new NotFoundException("Неизвестное название параметра запроса.");
            }
        }
        return itemSpecification;
    }

    public static Specification<Item> itemPriceWithinRange(double rangeStart, double rangeEnd) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("price"), rangeStart, rangeEnd);
    }

    public static Specification<Item> itemManufacturerInSet(Set<String> manufacturers) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.in(root.get("manufacturer")).value(manufacturers)
        );
    }

    public static Specification<Item> itemQuantityIsNotNull() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("quantity"));
    }

    public static Specification<Item> itemRatingIsMoreThan(double minRating) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("rating"), minRating);
    }

    public static Specification<Item> itemCategoryIs(Category category) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("category"), category);
    }

    public static Specification<Item> itemFeatureWithinRange(String featureRequestName, double rangeStart, double rangeEnd) {
        return (root, query, criteriaBuilder) -> {
            Join<Item, ItemFeature> itemAndItemFeatures = root.join("features");
            Join<ItemFeature, Feature> itemAndAllFeatures = itemAndItemFeatures.join("feature");

            Predicate predicate1 = criteriaBuilder.equal(itemAndAllFeatures.get("requestParamName"), featureRequestName);
            Predicate predicate2 = criteriaBuilder.between(itemAndItemFeatures.get("numericValue"), rangeStart, rangeEnd);

            return criteriaBuilder.and(predicate1, predicate2);
        };
    }

    public static Specification<Item> itemFeatureInSet(String featureRequestName, Set<String> values) {
        return (root, query, criteriaBuilder) -> {
            Join<Item, ItemFeature> itemAndItemFeatures = root.join("features");
            Join<ItemFeature, Feature> itemAndAllFeatures = itemAndItemFeatures.join("feature");

            Predicate predicate1 = criteriaBuilder.equal(itemAndAllFeatures.get("requestParamName"), featureRequestName);
            Predicate predicate2 = criteriaBuilder.in(itemAndItemFeatures.get("stringValue")).value(values);

            return criteriaBuilder.and(predicate1, predicate2);
        };
    }

    public static Specification<Item> itemFeatureFlagIs(String featureRequestName, boolean flag) {
        return (root, query, criteriaBuilder) -> {
            Join<Item, ItemFeature> itemAndItemFeatures = root.join("features");
            Join<ItemFeature, Feature> itemAndAllFeatures = itemAndItemFeatures.join("feature");

            Predicate predicate1 = criteriaBuilder.equal(itemAndAllFeatures.get("requestParamName"), featureRequestName);
            Predicate predicate2 = flag == true ?
                    criteriaBuilder.equal(itemAndItemFeatures.get("stringValue"), "Есть")
                    : // flag == false
                    criteriaBuilder.equal(itemAndItemFeatures.get("stringValue"), "Нет");

            return criteriaBuilder.and(predicate1, predicate2);
        };
    }
}
