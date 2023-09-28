package ru.devtrifanya.online_store.util.validators;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.devtrifanya.online_store.dto.FeatureDTO;
import ru.devtrifanya.online_store.repositories.FeatureRepository;
import ru.devtrifanya.online_store.util.exceptions.AlreadyExistException;

@Component
@Data
public class FeatureValidator {
    public final FeatureRepository featureRepository;

    public void validate(FeatureDTO featureDTO, int categoryid) {
        // TODO - сделать проверку, что у данной категории еще нет характеристики с таким названием
        if (false) {
            throw new AlreadyExistException("У текущей категории уже есть характеристика с указанным названием.");
        }
    }
}
