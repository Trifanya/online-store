package ru.devtrifanya.online_store.services;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;

import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.repositories.UserRepository;
import ru.devtrifanya.online_store.exceptions.NotFoundException;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Пользователь с указанным email не найден."));
    }

    public User getUser(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с указанным id не найден."));
    }

    public User updateUserInfo(User userToUpdate, User updatedUser) {
        updatedUser.setId(userToUpdate.getId());
        updatedUser.setRole(userToUpdate.getRole());

        return userRepository.save(updatedUser);
    }

    public void deleteUser(int userId) {
        userRepository.deleteById(userId);
    }
}
