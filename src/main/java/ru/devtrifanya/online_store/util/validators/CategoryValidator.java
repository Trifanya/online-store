package ru.devtrifanya.online_store.util.validators;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.devtrifanya.online_store.content.dto.CategoryDTO;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.repositories.CategoryRelationRepository;
import ru.devtrifanya.online_store.repositories.CategoryRepository;
import ru.devtrifanya.online_store.util.exceptions.AlreadyExistException;

import java.util.Optional;

@Component
@Data
public class CategoryValidator {
    private final CategoryRepository categoryRepository;
    private final CategoryRelationRepository categoryRelationRepository;

    /**
     * Проверка на наличие в текущей категории подкатегории с таким же названием, как и у добавляемой категории.
     * Если такая категория есть, то будет выброшено исключение.
     */
    public void validate(CategoryDTO categoryDTO, int parentId) {
        Optional<Category> child = categoryRepository.findByName(categoryDTO.getName());

        if (categoryRelationRepository.findByChildIdAndParentId(child.get().getId(), parentId).isPresent()) {
            throw new AlreadyExistException("В текущей категории уже есть подкатегория с указанным названием.");
        }
    }
}
