package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.models.Category;
import ru.devtrifanya.online_store.models.CategoryRelation;
import ru.devtrifanya.online_store.repositories.CategoryRelationRepository;
import ru.devtrifanya.online_store.repositories.CategoryRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@Data
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryRelationRepository categoryRelationRepository;

    public List<Category> getAll(int categoryId) {
        return categoryRelationRepository.findByParentId(categoryId);
    }

    @Transactional
    public void create(Category category, int parentId) {
        Category parent = categoryRepository.findById(parentId).orElse(null);
        CategoryRelation relation = new CategoryRelation(category, parent);
        categoryRelationRepository.save(relation);
        categoryRepository.save(category);
    }

}
