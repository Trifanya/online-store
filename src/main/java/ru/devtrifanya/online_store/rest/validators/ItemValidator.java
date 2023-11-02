package ru.devtrifanya.online_store.rest.validators;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.Feature;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.repositories.ItemRepository;
import ru.devtrifanya.online_store.exceptions.NotFoundException;
import ru.devtrifanya.online_store.repositories.FeatureRepository;
import ru.devtrifanya.online_store.repositories.CategoryRepository;
import ru.devtrifanya.online_store.exceptions.AlreadyExistException;
import ru.devtrifanya.online_store.rest.dto.entities_dto.ItemFeatureDTO;
import ru.devtrifanya.online_store.exceptions.UnavailableActionException;
import ru.devtrifanya.online_store.rest.dto.requests.AddOrUpdateItemRequest;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ItemValidator {
    private final ItemRepository itemRepository;
    private final FeatureRepository featureRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Валидация запроса на добавление нового товара.
     */
    public void performNewItemValidation(AddOrUpdateItemRequest request) {
        validateUniqueName(request.getItem().getName(), request.getItem().getId());
        validateCategoryIsFinal(request.getCategoryId());
        validateAllFeaturesSpecified(request.getCategoryId(), request.getItemFeatures());
        validateRelevantFeatures(request.getCategoryId(), request.getItemFeatures());
    }

    /**
     * Валидация запроса на обновление информации о товаре.
     */
    public void performUpdatedItemValidation(AddOrUpdateItemRequest request) {
        validateUniqueName(request.getItem().getName(), request.getItem().getId());
        validateCategoryIsFinal(request.getCategoryId());
        validateAllFeaturesSpecified(request.getCategoryId(), request.getItemFeatures());
        validateRelevantFeatures(request.getCategoryId(), request.getItemFeatures());
    }

    /**
     * Проверка названия товара на уникальность.
     * Если в БД уже есть товар с указанным названием, то выбрасывается исключение.
     */
    public void validateUniqueName(String name, int itemId) {
        Item namesake = itemRepository.findByName(name).orElse(null);
        if (namesake != null && namesake.getId() != itemId) {
            throw new AlreadyExistException("Товар с таким названием уже есть на сайте.");
        }
    }

    /**
     * Проверка категории, в которую добавляется товар.
     * Если категория, в которую добавляется товар, не является конечной, то выбрасывается исключение.
     */
    public void validateCategoryIsFinal(int categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (!category.getChildren().isEmpty()) {
            throw new UnavailableActionException("Нельзя добавить товар в промежуточную категорию.");
        }
    }

    /**
     * Проверка характеристик товара.
     * Если у добавляемого товара указаны не все характеристики, которые есть у категории
     * этого товара, то выбрасывается исключение.
     */
    public void validateAllFeaturesSpecified(int categoryId, Map<Integer, ItemFeatureDTO> itemFeatures) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с указанным id не найдена."));
        if (itemFeatures.size() < category.getFeatures().size()) {
            throw new UnavailableActionException("Необходимо указать значения всех характеристик товара.");
        }
    }

    /**
     * Проверка характеристик товара.
     * Если у категории, в которую добавляется товар, нет характеристики с id, указанным
     * в ключе переданной мапы с характеристиками, то выбрасывается исключение.
     */
    public void validateRelevantFeatures(int categoryId, Map<Integer, ItemFeatureDTO> itemFeatures) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с указанным id не найдена."));

        for (Map.Entry<Integer, ItemFeatureDTO> itemFeature : itemFeatures.entrySet()) {
            Feature feature = featureRepository.findById(itemFeature.getKey())
                    .orElseThrow(() -> new NotFoundException("Характеристика с указанным id не найдена."));
            if (!category.getFeatures().contains(feature)) {
                throw new UnavailableActionException("У товаров данной категории не может быть характеристики с указанным id.");
            }
        }
    }

}
