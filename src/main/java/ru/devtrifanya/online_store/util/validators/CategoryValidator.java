package ru.devtrifanya.online_store.util.validators;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.repositories.CategoryRelationRepository;
import ru.devtrifanya.online_store.repositories.CategoryRepository;

@Component
@Data
public class CategoryValidator {
    private final CategoryRepository categoryRepository;

    public void validate(Category category) {

    }
}
