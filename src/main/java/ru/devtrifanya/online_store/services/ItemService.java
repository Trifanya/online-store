package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.repositories.ItemRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Data
public class ItemService {
    private final ItemRepository itemRepository;

    public Optional<Item> findOne(int id) {
        return itemRepository.findById(id);
    }

    public Optional<Item> findOne(String name) {
        return itemRepository.findByName(name);
    }

    public List<Item> findItemsByCategory(String category) {
        return itemRepository.findByCategory(category);
    }

    public List<String> findSubcategoriesByCategory(String parentCategory) {
        return null;
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    @Transactional
    public void save(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void update(int id, Item item) {
        item.setId(id);
        itemRepository.save(item);
    }

    @Transactional
    public void delete(int id) {
        itemRepository.deleteById(id);
    }
}
