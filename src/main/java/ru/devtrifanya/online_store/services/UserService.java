package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.repositories.UserRepository;
import ru.devtrifanya.online_store.util.exceptions.NotFoundException;

@Service
@Transactional(readOnly = true)
@Data
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;


    @Override
    public User loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Пользователь с указанным email не найден."));

        return user;
    }

    @Transactional
    public User updateUserInfo(int userId, User updatedUser) {
        updatedUser.setId(userId);
        return userRepository.save(updatedUser);
    }

    @Transactional
    public void deleteUser(int userId) {
        userRepository.deleteById(userId);
    }
}
