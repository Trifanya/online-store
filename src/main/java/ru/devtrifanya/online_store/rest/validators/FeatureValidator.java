package ru.devtrifanya.online_store.rest.validators;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import ru.devtrifanya.online_store.models.Feature;
import ru.devtrifanya.online_store.repositories.FeatureRepository;
import ru.devtrifanya.online_store.rest.dto.entities_dto.FeatureDTO;
import ru.devtrifanya.online_store.exceptions.AlreadyExistException;

@Component
@RequiredArgsConstructor
public class FeatureValidator {
    public final FeatureRepository featureRepository;

    /**
     * Валидация запроса на добавление новой характеристики.
     */
    public void performNewFeatureValidation(FeatureDTO feature) {
        validateUniqueName(feature.getName(), feature.getId());
        validateUniqueRequestParamName(feature.getRequestParamName(), feature.getId());
    }

    /**
     * Валидация запроса на обновление информации о характеристике.
     */
    public void performUpdatedFeatureValidation(FeatureDTO feature) {
        validateUniqueName(feature.getName(), feature.getId());
        validateUniqueRequestParamName(feature.getRequestParamName(), feature.getId());
    }

    /**
     * Валидация имени характеристики.
     * Если в БД уже есть характеристика с указанным именем, то будет выброшено исключение.
     */
    public void validateUniqueName(String name, int featureId) {
        Feature namesake = featureRepository.findByName(name).orElse(null);
        if (namesake != null && namesake.getId() != featureId) {
            throw new AlreadyExistException("Характеристика с указанным названием уже существует.");
        }
    }

    /**
     * Валидация имени параметра запроса характеристики.
     * Если в БД уже есть характеристика с указанным именем параметра запроса, то будет
     * выброшено исключение.
     */
    public void validateUniqueRequestParamName(String requestParamName, int featureId) {
        Feature namesake = featureRepository.findByRequestParamName(requestParamName).orElse(null);
        if (namesake != null && namesake.getId() != featureId) {
            throw new AlreadyExistException("Характеристика с указанным псевдонимом уже существует.");
        }
    }
}
