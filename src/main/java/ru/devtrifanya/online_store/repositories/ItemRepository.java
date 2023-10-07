package ru.devtrifanya.online_store.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.devtrifanya.online_store.models.Item;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    Optional<Item> findByName(String name);
    List<Item> findAllByCategoryId(int categoryId);
    Page<Item> findAllByCategoryId(int categoryId, PageRequest pageRequest);
}
