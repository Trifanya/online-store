package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.repositories.UserRepository;
import ru.devtrifanya.online_store.util.exceptions.person.PersonNotFoundException;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Data
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public Optional<User> findOne(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public void update(int id, User user) {
        user.setId(id);
        userRepository.save(user);
    }

    @Override
    public User loadUserByUsername(String email) throws PersonNotFoundException {
        Optional<User> person = userRepository.findByEmail(email);

        if (person.isEmpty()) {
            throw new PersonNotFoundException("Пользователь с таким email не найден.");
        }

        return person.get();
    }

    @Transactional
    public void delete(int id) {
        userRepository.deleteById(id);
    }
}
