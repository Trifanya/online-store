package ru.devtrifanya.online_store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.devtrifanya.online_store.models.CartElement;

import java.util.List;

@Repository
public interface CartElementRepository extends JpaRepository<CartElement, Integer> {
    List<CartElement> findAllByUserId(int userId);
    int countAllByUserId(int userId);
    boolean existsByItemIdAndUserId(int itemId, int userId);
}
