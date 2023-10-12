package ru.devtrifanya.online_store.rest.validators;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.devtrifanya.online_store.repositories.CategoryRepository;
import ru.devtrifanya.online_store.rest.dto.requests.NewCategoryRequest;
import ru.devtrifanya.online_store.util.exceptions.AlreadyExistException;

@Component
@Data
public class CategoryValidator {
    private final CategoryRepository categoryRepository;

    public void validate(NewCategoryRequest request) {
        if (categoryRepository.findByName(request.getCategory().getName()).isPresent()) {
            throw new AlreadyExistException("Категория с таким названием уже есть в каталоге.");
        }
    }
}
