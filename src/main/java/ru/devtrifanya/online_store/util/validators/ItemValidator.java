package ru.devtrifanya.online_store.util.validators;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.devtrifanya.online_store.dto.ItemDTO;
import ru.devtrifanya.online_store.models.Feature;
import ru.devtrifanya.online_store.repositories.FeatureRepository;
import ru.devtrifanya.online_store.repositories.ItemRepository;
import ru.devtrifanya.online_store.util.exceptions.AlreadyExistException;
import ru.devtrifanya.online_store.util.exceptions.UnavailableActionException;

import java.util.List;

@Component
@Data
public class ItemValidator {
    private final ItemRepository itemRepository;
    private final FeatureRepository featureRepository;

    /**
     * Если у товара указаны не все характеристики, которые есть у категории этого
     * товара, то выбрасывается исключение.
     * Если товар с таким же названием уже есть в БД, то выбрасывается исключение.
     */
    public void validate(ItemDTO itemDTO, int categoryId) {
        // Валидация характеристик товара
        List<Feature> categoryFeatures = featureRepository.findAllByCategoryId(categoryId);
        if (itemDTO.getFeatures().size() != categoryFeatures.size()) {
            throw new UnavailableActionException("У товара должны быть указаны все характеристики.");
        }
        // Валидация имени товара
        if (itemRepository.findByName(itemDTO.getName()).isPresent()) {
            throw new AlreadyExistException("Товар с таким названием уже есть на сайте.");
        }
    }

}
