package ru.devtrifanya.online_store.rest.validators;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.repositories.CategoryRepository;
import ru.devtrifanya.online_store.exceptions.AlreadyExistException;
import ru.devtrifanya.online_store.rest.dto.entities_dto.CategoryDTO;
import ru.devtrifanya.online_store.exceptions.UnavailableActionException;
import ru.devtrifanya.online_store.rest.dto.requests.AddOrUpdateCategoryRequest;
import ru.devtrifanya.online_store.rest.utils.MainClassConverter;

@Component
@RequiredArgsConstructor
public class CategoryValidator {
    private final FeatureValidator featureValidator;

    private final CategoryRepository categoryRepository;

    private final MainClassConverter converter;

    /**
     * Валидация запроса на добавление новой категории.
     */
    public void performNewCategoryValidation(AddOrUpdateCategoryRequest request) {
        validateUniqueName(request.getCategory());
        validateParentIsNotFinal(request.getNewParentId());
        // если в новую категорию добавляются новые характеристики, то нужно провалидировать каждую из них
        if (!request.getNewFeatures().isEmpty()) {
            request.getNewFeatures().forEach(featureValidator::performNewFeatureValidation);
        }
    }

    /**
     * Валидация запроса на обновление информации о категории.
     */
    public void performUpdatedCategoryValidation(AddOrUpdateCategoryRequest request) {
        validateUniqueName(request.getCategory());
        // если категория была перемещена
        if (request.getPrevParentId() != request.getNewParentId()) {
            validateParentIsNotFinal(request.getNewParentId());
            validateParentIsNotChild(
                    converter.convertToCategory(request.getCategory()),
                    categoryRepository.findById(request.getNewParentId()).orElse(null)
            );
        }
        // если в конечную категорию добавляются новые характеристики, то нужно провалидировать каждую из них
        // если новые характеристики добавляются в неконечную категорию, то выбрасывается исключение
        Category category = categoryRepository.findById(request.getCategory().getId()).orElse(null);
        if (!request.getNewFeatures().isEmpty() || request.getExistingFeaturesId().length > 0) { // если в категорию добавляются новые характеристики
            if (!category.getChildren().isEmpty()) { // если категория является конечной
                throw new UnavailableActionException("Нельзя добавить характеристику промежуточной категории.");
            } else {
                request.getNewFeatures().forEach(featureValidator::performNewFeatureValidation);
            }
        }
    }

    /**
     * Валидация имени категории.
     * Если в БД уже есть категория с указанным именем, то будет выброшено исключение.
     */
    public void validateUniqueName(CategoryDTO category) {
        Category namesake = categoryRepository.findByName(category.getName()).orElse(null);
        if (namesake != null && category.getId() != namesake.getId()) {
            throw new AlreadyExistException("Категория с указанным названием уже есть в каталоге.");
        }
    }

    /**
     * Валидация родительской категории.
     * Если категория, в которую осуществляется добавление или перемещение категории,
     * является конечной, то будет выброшено исключение.
     */
    public void validateParentIsNotFinal(int newParentId) {
        Category newParent = categoryRepository.findById(newParentId).orElse(null);
        if (newParent != null && !newParent.getItems().isEmpty()) {
            throw new UnavailableActionException("Конечная категория не может содержать другие категории.");
        }
    }

    /**
     * Валидация родительской категории.
     * Если категория, в которую была перемещена обновленная категория, является дочерней
     * категорией обновленной категории непосредственно или посредством других ее дочерних
     * категорий, то выбрасывается исключение.
     */
    public void validateParentIsNotChild(Category updatedCategory, Category parent) {
        for (Category parentOfParent : parent.getParents()) {
            if (parentOfParent.equals(updatedCategory)) {
                throw new UnavailableActionException("Нельзя переместить категорию в дочернюю категорию.");
            } else {
                validateParentIsNotChild(updatedCategory, parentOfParent);
            }
        }
    }

}
