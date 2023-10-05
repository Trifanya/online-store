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

    /**
     * Проверка на наличие у текущей категории характеристики с таким же названием
     * как у добавляемой в эту категорию характеристики.
     */
    public void validate(FeatureDTO featureDTO, int categoryId) {
        if (featureRepository.findByNameAndCategoryId(featureDTO.getName(), categoryId).isPresent()) {
            throw new AlreadyExistException("У текущей категории уже есть характеристика с указанным названием.");
        }
    }
}
