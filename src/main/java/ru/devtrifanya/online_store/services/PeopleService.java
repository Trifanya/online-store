package ru.devtrifanya.online_store.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder, BCryptPasswordEncoder passwordEncoder1) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder1;
    }

    public Optional<Person> findOne(int id) {
        return peopleRepository.findById(id);
    }

    public Optional<Person> findOne(String email) {
        return peopleRepository.findByEmail(email);
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    @Transactional
    public void signUpPerson(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");
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
