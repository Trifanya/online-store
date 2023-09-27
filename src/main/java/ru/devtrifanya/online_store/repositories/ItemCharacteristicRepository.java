package ru.devtrifanya.online_store.repositories;

import jakarta.persistence.Column;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.devtrifanya.online_store.models.ItemCharacteristic;

@Repository
public interface ItemCharacteristicRepository extends JpaRepository<ItemCharacteristic, Integer> {
}
