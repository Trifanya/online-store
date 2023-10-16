package ru.devtrifanya.online_store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.devtrifanya.online_store.models.ItemImage;

import java.util.List;
import java.util.Optional;


@Repository
public interface ItemImageRepository extends JpaRepository<ItemImage, Integer> {
    Optional<ItemImage> findByUrl(String url);
}
