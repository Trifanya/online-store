package ru.devtrifanya.online_store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.devtrifanya.online_store.models.Feature;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeatureRepository extends JpaRepository<Feature, Integer> {
    Optional<Feature> findByName(String name);
    Optional<Feature> findByNameAndCategoryId(String featureName, int categoryid);
    List<Feature> findAllByCategoryId(int categoryId);
}