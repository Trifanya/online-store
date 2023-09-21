package ru.devtrifanya.online_store.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.models.Person;
import ru.devtrifanya.online_store.repositories.PeopleRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;
    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }
    public Optional<Person> findOne(int id) {
        return peopleRepository.findById(id);
    }
    public List<Person> findAll() {
        return peopleRepository.findAll();
    }
    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }
    @Transactional
    public void update(int id, Person person) {
        person.setId(id);
        peopleRepository.save(person);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }
}
