package ru.devtrifanya.online_store.security;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.devtrifanya.online_store.models.Person;
import ru.devtrifanya.online_store.repositories.PeopleRepository;
import ru.devtrifanya.online_store.security.PersonDetails;
import ru.devtrifanya.online_store.util.exceptions.person.PersonNotFoundException;

import java.util.Optional;

@Service
@Data
public class PersonDetailsService implements UserDetailsService {
    private final PeopleRepository peopleRepository;

    @Override
    public PersonDetails loadUserByUsername(String email) {
        Optional<Person> person = peopleRepository.findByEmail(email);

        if (person.isEmpty()) {
            throw new PersonNotFoundException("Пользователь с таким email не найден.");
        }

        return new PersonDetails(person.get());
    }


}
