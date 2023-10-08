package ru.devtrifanya.online_store.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.devtrifanya.online_store.models.Item;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    Optional<Item> findByName(String name);

    List<Item> findAllByCategoryId(int categoryId);

    Page<Item> findAllByCategoryId(int categoryId, PageRequest pageRequest);

    @Query(value = "select * from (item join (feature join item_feature on feature.id = item_feature.feature_id) " +
            "as features on item.id = features.item_id) where features.name = ? and features.value in ?",
            nativeQuery = true)
    List<Item> findItemsWithFeatureInRange(String featureName, Set<String> featureValues);
}
