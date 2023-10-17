package ru.devtrifanya.online_store.rest.validators;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.repositories.CategoryRepository;
import ru.devtrifanya.online_store.exceptions.AlreadyExistException;
import ru.devtrifanya.online_store.rest.dto.requests.AddOrUpdateCategoryRequest;

@Component
@RequiredArgsConstructor
public class CategoryValidator {
    private final CategoryRepository categoryRepository;

    public void validate(AddOrUpdateCategoryRequest request) {
        Category category = categoryRepository.findByName(request.getCategory().getName()).orElse(null);
        if (category != null && category.getId() != request.getCategory().getId()) {
            throw new AlreadyExistException("Категория с таким названием уже есть в каталоге.");
        }
    }
}
