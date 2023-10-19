package ru.devtrifanya.online_store.rest.validators;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import ru.devtrifanya.online_store.rest.dto.requests.AddItemRequest;
import ru.devtrifanya.online_store.rest.dto.entities_dto.ItemFeatureDTO;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.Feature;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.repositories.ItemRepository;
import ru.devtrifanya.online_store.repositories.FeatureRepository;
import ru.devtrifanya.online_store.repositories.CategoryRepository;
import ru.devtrifanya.online_store.exceptions.NotFoundException;
import ru.devtrifanya.online_store.exceptions.AlreadyExistException;
import ru.devtrifanya.online_store.exceptions.UnavailableActionException;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ItemValidator {
    private final ItemRepository itemRepository;
    private final FeatureRepository featureRepository;
    private final CategoryRepository categoryRepository;

    public void validate(AddItemRequest request) {
        // Проверка названия товара на уникальность
        Item item = itemRepository.findByName(request.getItem().getName()).orElse(null);
        if (item != null && item.getId() != request.getItem().getId()) {
            throw new AlreadyExistException("Товар с таким названием уже есть на сайте.");
        }
        // Категория, в которую добавляется товар должна быть конечной
        if (!categoryRepository.findById(request.getCategoryId()).get().getItems().isEmpty()) {
            throw new UnavailableActionException("Нельзя добавить товар в промежуточную категорию.");
        }
        // У добавляемого товара должны быть указаны все характеристики, которые есть у категории, в которую он добавляется
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Категория с указанным id не найдена."));
        if (request.getItemFeatures().size() < category.getFeatures().size()) {
            throw new UnavailableActionException("Необходимо указать значения всех характеристик товара.");
        }

        // У категории, в которую добавляется товар, должна быть характеристика с id из ключа мапы.
        // В противном случае товару будет назначаться значение характеристики, которой у него быть не должно.
        for (Map.Entry<Integer, ItemFeatureDTO> itemFeature : request.getItemFeatures().entrySet()) {
            Feature feature = featureRepository.findById(itemFeature.getKey())
                    .orElseThrow();
            if (!category.getFeatures().contains(feature)) {
                throw new UnavailableActionException("У товаров данной категории не может быть характеристики с указанным id.");
            }
        }
    }



}
