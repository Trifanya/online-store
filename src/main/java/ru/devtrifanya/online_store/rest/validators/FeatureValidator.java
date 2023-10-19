package ru.devtrifanya.online_store.rest.validators;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import ru.devtrifanya.online_store.repositories.FeatureRepository;
import ru.devtrifanya.online_store.rest.dto.entities_dto.FeatureDTO;
import ru.devtrifanya.online_store.exceptions.AlreadyExistException;

@Component
@RequiredArgsConstructor
public class FeatureValidator {
    public final FeatureRepository featureRepository;

    public void validateNewFeature(FeatureDTO feature) {
        validateFeatureName(feature.getName());
        validateFeatureRequestParamName(feature.getRequestParamName());
    }

    public void validateFeatureName(String name) {
        if (featureRepository.findByName(name).isPresent()) {
            throw new AlreadyExistException("Характеристика с указанным названием уже существует.");
        }
    }

    public void validateFeatureRequestParamName(String requestParamName) {
        if (featureRepository.findByRequestParamName(requestParamName).isPresent()) {
            throw new AlreadyExistException("Характеристика с указанным псевдонимом уже существует.");
        }
    }
}
