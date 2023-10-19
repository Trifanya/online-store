package ru.devtrifanya.online_store.rest.validators;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import ru.devtrifanya.online_store.exceptions.UnavailableActionException;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.repositories.CategoryRepository;
import ru.devtrifanya.online_store.exceptions.AlreadyExistException;
import ru.devtrifanya.online_store.rest.dto.entities_dto.CategoryDTO;
import ru.devtrifanya.online_store.rest.dto.requests.AddOrUpdateCategoryRequest;

@Component
@RequiredArgsConstructor
public class CategoryValidator {
    private final FeatureValidator featureValidator;

    private final CategoryRepository categoryRepository;

    public void validateNewCategory(AddOrUpdateCategoryRequest request) {
        validateName(request.getCategory());

        validateParent(request.getNewParentId());

        // если в новую категорию добавляются новые характеристики, то нужно провалидировать каждую из них
        if (!request.getNewFeatures().isEmpty()) {
            request.getNewFeatures().forEach(featureValidator::validateNewFeature);
        }
    }

    public void validateUpdatedCategory(AddOrUpdateCategoryRequest request) {
        validateName(request.getCategory());

        // если категория была перемещена
        if (request.getPrevParentId() != request.getNewParentId()) {
            validateParent(request.getNewParentId());
        }

        // если в конечную категорию добавляются новые характеристики, то нужно провалидировать каждую из них
        // если новые характеристики добавляются в неконечную категорию, то выбрасывается исключение
        Category category = categoryRepository.findById(request.getCategory().getId()).orElse(null);
        if (!request.getNewFeatures().isEmpty() && category.getChildren().isEmpty()) { // если категория является конечной
            request.getNewFeatures().forEach(featureValidator::validateNewFeature);
        } else {
            throw new UnavailableActionException("Нельзя добавить характеристику промежуточной категории.");
        }
    }

    /**
     * Валидация имени категории.
     * Если в БД уже есть категория с указанным именем, то будет выброшено исключение.
     */
    public void validateName(CategoryDTO category) {
        Category sameNameCategory = categoryRepository.findByName(category.getName()).orElse(null);
        if (sameNameCategory != null && category.getId() != sameNameCategory.getId()) {
            throw new AlreadyExistException("Категория с указанным названием уже есть в каталоге.");
        }
    }

    /**
     * Валидация родительской категории.
     * Если категория, в которую осуществляется добавление или перемещение категории,
     * является конечной, то будет выброшено исключение.
     */
    public void validateParent(int newParentId) {
        Category newParent = categoryRepository.findById(newParentId).orElse(null);
        if (newParent != null && !newParent.getItems().isEmpty()) {
            throw new UnavailableActionException("Конечная категория не может содержать другие категории.");
        }
    }

}
