package ru.devtrifanya.online_store.util.validators;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.devtrifanya.online_store.content.dto.FeatureDTO;
import ru.devtrifanya.online_store.repositories.FeatureRepository;
import ru.devtrifanya.online_store.util.exceptions.AlreadyExistException;

@Component
@Data
public class FeatureValidator {
    public final FeatureRepository featureRepository;

}
