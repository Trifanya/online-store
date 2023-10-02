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
import ru.devtrifanya.online_store.util.exceptions.NotFoundException;
import ru.devtrifanya.online_store.util.exceptions.UnavailableActionException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Data
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryRelationRepository categoryRelationRepository;
    private final ItemRepository itemRepository;

    /**
     * Получение всех подкатегорий категории с указанным id.
     * Если какая-то из подкатегорий не найдена по id, то выбрасывается исключение.
     */
    public List<Category> getSubcategories(int categoryId) {
        return categoryRelationRepository.findAllByParentId(categoryId)
                .stream()
                .map(relation -> categoryRepository.findById(relation.getChild().getId()))
                .map(optional -> optional
                        .orElseThrow(() -> new NotFoundException("Подкатегория не найдена.")))
                .collect(Collectors.toList());
    }

    @Transactional
    public void createNewCategory(Category category, int parentId) {
        Category parent = categoryRepository.findById(parentId)
                .orElseThrow(() -> new NotFoundException("Категория с таким id не найдена."));

        CategoryRelation relation = new CategoryRelation();
        relation.setParent(parent);
        relation.setChild(category);
        categoryRelationRepository.save(relation);

        categoryRepository.save(category);
    }

    /**
     * Изменение названия категории.
     */
    @Transactional
    public void updateCategoryInfo(Category category, int categoryId) {
        category.setId(categoryId);
        categoryRepository.save(category);
    }

    /**
     * Если удаляемая категория содержит только другие категории, то нужно
     * связать дочерние категории этой категории с ее родительской категорией.
     * Если удаляемая категория является конечной, то есть содержит в себе
     * только товары, то нужно удалить все товары удаляемой категории.
     */
    @Transactional
    public void deleteCategory(int categoryId) {
        // Если категория содержит только другие категории
        if (!categoryRelationRepository.findAllByParentId(categoryId).isEmpty()) {
            CategoryRelation relationWithParent = categoryRelationRepository.findByChildId(categoryId)
                    .orElseThrow(() -> new NotFoundException("У данной категории нет родительской категории."));
            Category parent = categoryRepository.findById(relationWithParent.getParent().getId())
                    .orElseThrow(() -> new NotFoundException("Родительская категория удаляемой категории не найдена."));

            List<CategoryRelation> relationsWithChildren = categoryRelationRepository.findAllByParentId(categoryId);
            relationsWithChildren
                    .stream()
                    .forEach(relation -> {
                        relation.setParent(parent);
                        categoryRelationRepository.save(relation);
                    });
        }
        categoryRepository.deleteById(categoryId);
    }

}
