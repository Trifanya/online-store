package ru.devtrifanya.online_store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.devtrifanya.online_store.models.Person;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
}
