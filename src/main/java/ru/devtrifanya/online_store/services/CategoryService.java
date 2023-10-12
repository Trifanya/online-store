package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.models.CategoryRelation;
import ru.devtrifanya.online_store.models.Feature;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.repositories.CategoryRelationRepository;
import ru.devtrifanya.online_store.repositories.CategoryRepository;
import ru.devtrifanya.online_store.repositories.ItemRepository;
import ru.devtrifanya.online_store.util.exceptions.NotFoundException;
import ru.devtrifanya.online_store.util.exceptions.UnavailableActionException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Data
public class CategoryService {
    private final CategoryRelationService categoryRelationService;
    private final CategoryRepository categoryRepository;
    private final CategoryRelationRepository categoryRelationRepository;

    /**
     * Получение категории по ее id.
     * Метод получает на вход id категории, затем вызывает метод репозитория для получения
     * категории по id и возвращает найденную категорию.
     * Если категория с указанным id не найдена в БД, то выбрасывается исключение.
     */
    public Category getCategory(int categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с указанным id не найдена."));
    }

    public List<Category> getTopCategories() {
        return categoryRelationRepository.parentIdIsNull()
                .stream()
                .map(relation -> relation.getChild())
                .collect(Collectors.toList());
    }

    @Transactional
    public Category createNewCategory(int parentCategoryId, Category categoryToSave, List<Feature> features) {
        categoryRelationService.createCategoryRelation(
                categoryToSave,
                categoryRepository.findById(parentCategoryId).orElse(null)
        );
        categoryToSave.setId(0);
        categoryToSave.setFeatures(features);

        return categoryRepository.save(categoryToSave);
    }


    @Transactional
    public Category updateCategory(Category updatedCategory, List<Feature> features) {
        updatedCategory.setFeatures(features);
        return categoryRepository.save(updatedCategory);
    }


    @Transactional
    public void deleteCategory(int categoryId) {
        categoryRepository.deleteById(categoryId);
    }

}
