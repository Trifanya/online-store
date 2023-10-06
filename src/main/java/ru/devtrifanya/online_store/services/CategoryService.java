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
    private final ItemService itemService;
    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;

    public Category getCategory(int categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с указанным id не найдена."));
    }

    /**
     * Данный метод находит в БД категорию по указанному id, затем находит в БД товары, принадлежащие
     * этой категории, которые нужно отобразить согласно указанным номеру страницы, количеству
     * товаров на странице и критерию сортировки товаров, далее присваивает найденной категории
     * найденный набор товаров и возвращает объект категории.
     * Если категория с указанным id не найдена, то будет выброшено исключение NotFoundException.
     * Если у категории нет товаров, то ей будет присвоен пустой список товаров.
     */
    public Category getCategory(int categoryId, int pageNum, int itemsPerPage, String sortBy) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с указанным id не найдена."));

        category.setItems(
                itemService.getItemsByCategory(
                        categoryId,
                        pageNum,
                        itemsPerPage,
                        sortBy)
        );

        return category;
    }

    @Transactional
    public Category createNewCategory(Category categoryToSave, int parentId) {
        //categoryToSave.setFeatures();


        Category parent = categoryRepository.findById(parentId)
                .orElseThrow(() -> new NotFoundException("Категория с таким id не найдена."));

        CategoryRelation relation = new CategoryRelation();
        relation.setParent(parent);
        relation.setChild(categoryToSave);
        categoryRelationRepository.save(relation);

        categoryRepository.save(categoryToSave);
        return null;
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
