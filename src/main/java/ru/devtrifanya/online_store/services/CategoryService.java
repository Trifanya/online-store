package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.models.CategoryRelation;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.repositories.CategoryRelationRepository;
import ru.devtrifanya.online_store.repositories.CategoryRepository;
import ru.devtrifanya.online_store.repositories.ItemRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Data
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryRelationRepository categoryRelationRepository;
    private final ItemRepository itemRepository;

    /** Получение всех подкатегорий категории с указанным id. */
    public List<Category> getSubcategories(int categoryId) {
        return categoryRelationRepository.findByParentId(categoryId);
    }

    @Transactional
    public void create(Category category, int parentId) {
        Category parent = categoryRepository.findById(parentId).orElse(null);
        CategoryRelation relation = new CategoryRelation(category, parent);
        categoryRelationRepository.save(relation);
        categoryRepository.save(category);
    }

    @Transactional
    public void update(Category category, int categoryId) {
        category.setId(categoryId);
        categoryRepository.save(category);
    }

    /**
     * Если удаляемая категория содержит в себе только подкатегории, то нужно
     * связать эти подкатегории с родительской категорией удаляемой категории.
     * Если удаляемая категория является конечной, то есть содержит в себе
     * только товары, то нужно удалить все товары удаляемой категории.
     */
    @Transactional
    public void delete(int categoryId) {
        // TODO
    }

}
