package ru.devtrifanya.online_store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.devtrifanya.online_store.models.ReviewImage;

@Repository
public interface ReviewImageRepository extends JpaRepository<ReviewImage, Integer> {
}
