package ru.devtrifanya.online_store.rest.validators;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.devtrifanya.online_store.repositories.FeatureRepository;
import ru.devtrifanya.online_store.rest.dto.entities_dto.FeatureDTO;
import ru.devtrifanya.online_store.exceptions.AlreadyExistException;

@Component
@Data
public class FeatureValidator {
    public final FeatureRepository featureRepository;

    public void validate(FeatureDTO feature) {
        if (featureRepository.findByName(feature.getName()).isPresent()) {
            throw new AlreadyExistException("Характеристика с указанным названием уже существует.");
        }

        if (featureRepository.findByRequestParamName(feature.getRequestParamName()).isPresent()) {
            throw new AlreadyExistException("Характеристика с указанным псевдонимом уже существует.");
        }
    }
}
