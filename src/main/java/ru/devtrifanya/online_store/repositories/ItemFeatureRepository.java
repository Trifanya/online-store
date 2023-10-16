package ru.devtrifanya.online_store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.devtrifanya.online_store.models.ItemFeature;

import java.util.List;

@Repository
public interface ItemFeatureRepository extends JpaRepository<ItemFeature, Integer> {
    List<ItemFeature> findAllByFeatureId(int featureId);
    List<ItemFeature> findAllByItemId(int itemId);
}
