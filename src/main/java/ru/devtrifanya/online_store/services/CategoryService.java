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
    private final FeatureService featureService;
    private final ItemService itemService;
    private final CategoryRelationService categoryRelationService;
    private final CategoryRepository categoryRepository;

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

    /**
     * Получение категории по ее id с частью списка товаров.
     * Метод получает на вход id категории и параметры списка товаров категории, вызывает
     * метод репозитория для получения категории по id, затем вызывает метод сервиса,
     * возвращающий часть списка товаров найденной категории, далее инициализирует поле items
     * найденной категории возвращенным из сервиса списком и возвращает найденную категорию.
     * Если категория с указанным id не найдена в БД, то выбрасывается исключение.
     */
    public Category getCategory(int categoryId, int pageNum, int itemsPerPage, String sortBy, Map<String, String> allParams) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с указанным id не найдена."));

        if (!category.getItems().isEmpty()) {
            /*category.setItems(
                    itemService.getItemsByCategoryId(
                            categoryId,
                            pageNum,
                            itemsPerPage,
                            sortBy)
            );*/
            category.setItems(
                    itemService.getItemsByCategoryId(allParams)
            );
        }
        return category;
    }

    /**
     * Добавление новой категории.
     * Метод получает на вход категорию, которую нужно сохранить и id ее родительской
     * категории, затем вызывает метоод репозитория для сохранения категории в БД (это
     * нужно сделать до остальных действия, чтобы сохраняемой категории присвоился ненулевой
     * id), далее вызывает методы сервисов для сохранения характеристик категории, затем
     * создает новое отношение сохраненной категории с ее родительской категорией и наконец
     * возвращает сохраненную категорию.
     */
    @Transactional
    public Category createNewCategory(Category categoryToSave, int parentId) {
        Category savedCategory = categoryRepository.save(categoryToSave);

        for (Feature feature : savedCategory.getFeatures()) {
            featureService.createNewFeature(feature, savedCategory);
        }
        /*for (Item item : savedCategory.getItems()) {
            itemService.createNewItem(item, savedCategory);
        }*/

        CategoryRelation relation = new CategoryRelation();
        relation.setChild(savedCategory);
        categoryRelationService.createCategoryRelation(relation, categoryRepository.findById(parentId).get());

        return savedCategory;
    }

    /**
     * Обновление категории.
     * Метод получает на вход id категории, которую нужно обновить, саму категорию и id ее
     * родительской категории, затем инициализирует поле id обновляемой категории, в цикле
     * обращается к методу сервиса, который обновляет каждую из характеристик обновляемой
     * категории, затем вызывает метод репозитория для сохранения обновленной категории и
     * возвращает обновленную категорию.
     * Список товаров и список отношений с дочерними и родительскими категориями при этом
     * у обновляемой категории не меняется.
     */
    @Transactional
    public Category updateCategory(int categoryId, Category updatedCategory) {
        updatedCategory.setId(categoryId);

        List<Feature> oldFeatures = featureService.getFeaturesByCategoryId(categoryId);
        List<Feature> updatedFeatures = updatedCategory.getFeatures();
        for (int i = 0; i < updatedCategory.getFeatures().size(); i++) {
            featureService.updateFeatureInfo(
                    oldFeatures.get(i).getId(),
                    updatedFeatures.get(i),
                    updatedCategory
            );
        }
        return categoryRepository.save(updatedCategory);
    }

    /**
     * Удаление категории.
     * Метод получает на вход id удаляемой категории, затем вызывает метод репозитория
     * для получения удаляемой категории, далее проверяет, является ли категория конечной:
     * если нет - то нужно вызвать метод сервиса, который связывает дочерние категории
     * удаляемой категории с родительскими категориями удаляемой категории, если да -
     * то вышеописанное действие не нужно и можно сразу вызвать метод репозитория для
     * удаления категории с указанным id.
     */
    @Transactional
    public void deleteCategory(int categoryId) {
        Category categoryToDelete = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с указанным id не найдена."));

        if (categoryToDelete.getItems().isEmpty()) {
            categoryRelationService.updateRelationsOfDeletingCategory(categoryId);
        }

        categoryRepository.deleteById(categoryId);
    }

}
