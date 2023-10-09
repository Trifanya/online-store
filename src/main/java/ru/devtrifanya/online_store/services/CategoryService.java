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
    public Category createNewCategory(Category categoryToSave, List<Feature> features) {
        for (Feature feature : features) {
            categoryToSave.getFeatures().add(feature);
        }
        return categoryRepository.save(categoryToSave);
    }


    @Transactional
    public Category updateCategory(int categoryId, Category updatedCategory, List<Feature> newFeatures) {
        Category oldCategory = categoryRepository.findById(categoryId).
                orElseThrow(() -> new NotFoundException("Категория с указанным id не найдена."));

        updatedCategory.setFeatures(oldCategory.getFeatures());
        for (Feature feature : newFeatures) {
            updatedCategory.getFeatures().add(feature);
        }
        updatedCategory.setId(categoryId);

        return categoryRepository.save(updatedCategory);
    }


    @Transactional
    public void deleteCategory(int categoryId) {
        categoryRepository.deleteById(categoryId);
    }

}
