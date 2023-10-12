package ru.devtrifanya.online_store.rest.validators;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.devtrifanya.online_store.repositories.FeatureRepository;

@Component
@Data
public class FeatureValidator {
    public final FeatureRepository featureRepository;

}
