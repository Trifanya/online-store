package ru.devtrifanya.online_store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.devtrifanya.online_store.models.Review;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Optional<Review> findByItemIdAndUserId(int itemId, int userId);

    List<Review> findByItemId(int id);

    List<Review> findByItemIdOrderByStarsAsc(int id);

    List<Review> findByItemIdOrderByStarsDesc(int id);
}
