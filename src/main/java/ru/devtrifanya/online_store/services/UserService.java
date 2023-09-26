package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.repositories.UserRepository;
import ru.devtrifanya.online_store.security.jwt.JWTUtils;
import ru.devtrifanya.online_store.util.exceptions.person.PersonNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Data
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    /*public Optional<User> findOne(int id) {
        return userRepository.findById(id);
    }*/

    public Optional<User> findOne(String email) {
        return userRepository.findByEmail(email);
    }

    /*public List<User> findAll() {
        return userRepository.findAll();
    }*/

    /*@Transactional
    public void signUpPerson(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);
    }*/

    @Transactional
    public void update(int id, User user) {
        user.setId(id);
        userRepository.save(user);
    }

    @Transactional
    public void delete(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public User loadUserByUsername(String email) {
        Optional<User> person = userRepository.findByEmail(email);

        if (person.isEmpty()) {
            throw new PersonNotFoundException("Пользователь с таким email не найден.");
        }
        return person.get();
    }
}
