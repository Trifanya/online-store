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

    public void validateNewFeature(FeatureDTO feature) {
        validateUniqueName(feature.getName(), feature.getId());
        validateUniqueRequestParamName(feature.getRequestParamName(), feature.getId());
    }

    public void validateUpdatedFeature(FeatureDTO feature) {
        validateUniqueName(feature.getName(), feature.getId());
        validateUniqueRequestParamName(feature.getRequestParamName(), feature.getId());
    }

    public void validateUniqueName(String name, int featureId) {
        Feature namesake = featureRepository.findByName(name).orElse(null);
        if (namesake != null && namesake.getId() != featureId) {
            throw new AlreadyExistException("Характеристика с указанным названием уже существует.");
        }
    }

    public void validateUniqueRequestParamName(String requestParamName, int featureId) {
        Feature namesake = featureRepository.findByRequestParamName(requestParamName).orElse(null);
        if (namesake != null && namesake.getId() != featureId) {
            throw new AlreadyExistException("Характеристика с указанным псевдонимом уже существует.");
        }
    }
}
