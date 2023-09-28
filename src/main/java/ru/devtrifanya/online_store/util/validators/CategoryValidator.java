package ru.devtrifanya.online_store.util.validators;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.repositories.CategoryRepository;
import ru.devtrifanya.online_store.util.exceptions.AlreadyExistException;

@Component
@Data
public class CategoryValidator {
    private final CategoryRepository categoryRepository;

    public void validate(Category category) {
        // TODO - сделать проверку, что у текущей категории еще нет подкатегории с таким названием
        if (false) {
            throw new AlreadyExistException("В текущей категории уже есть подкатегория с указанным названием.");
        }
    }
}
