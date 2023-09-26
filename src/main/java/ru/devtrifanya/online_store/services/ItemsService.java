package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.repositories.ItemsRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Data
public class ItemsService {
    private final ItemsRepository itemsRepository;

    public Optional<Item> findOne(int id) {
        return itemsRepository.findById(id);
    }

    public Optional<Item> findOne(String name) {
        return itemsRepository.findByName(name);
    }

    public List<Item> findItemsByCategory(String category) {
        return itemsRepository.findByCategory(category);
    }

    public List<String> findSubcategoriesByCategory(String parentCategory) {
        return null;
    }

    public List<Item> findAll() {
        return itemsRepository.findAll();
    }

    @Transactional
    public void save(Item item) {
        itemsRepository.save(item);
    }

    @Transactional
    public void update(int id, Item item) {
        item.setId(id);
        itemsRepository.save(item);
    }

    @Transactional
    public void delete(int id) {
        itemsRepository.deleteById(id);
    }
}
