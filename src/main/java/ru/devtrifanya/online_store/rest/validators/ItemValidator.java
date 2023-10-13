package ru.devtrifanya.online_store.rest.validators;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.devtrifanya.online_store.repositories.CategoryRelationRepository;
import ru.devtrifanya.online_store.repositories.FeatureRepository;
import ru.devtrifanya.online_store.repositories.ItemRepository;
import ru.devtrifanya.online_store.rest.dto.requests.NewItemRequest;
import ru.devtrifanya.online_store.exceptions.AlreadyExistException;
import ru.devtrifanya.online_store.exceptions.UnavailableActionException;

@Component
@Data
public class ItemValidator {
    private final ItemRepository itemRepository;
    private final FeatureRepository featureRepository;
    private final CategoryRelationRepository categoryRelationRepository;

    public void validate(NewItemRequest request) {
        // Проверка названия товара на уникальность
        if (itemRepository.findByName(request.getItem().getName()).isPresent()) {
            throw new AlreadyExistException("Товар с таким названием уже есть на сайте.");
        }
        // Категория, в которую добавляется товар должна быть конечной
        if (categoryRelationRepository.existsByParentId(request.getCategoryId())) {
            throw new UnavailableActionException("Нельзя добавить товар в промежуточную категорию.");
        }
    }

}
