package ru.devtrifanya.online_store.rest.validators;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.repositories.UserRepository;
import ru.devtrifanya.online_store.rest.dto.entities_dto.UserDTO;
import ru.devtrifanya.online_store.exceptions.AlreadyExistException;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository userRepository;

    public void validate(UserDTO user, int userId) {
        User namesake = userRepository.findByEmail(user.getEmail()).orElse(null);
        if (namesake != null && namesake.getId() != userId) {
            throw new AlreadyExistException("Пользователь с указанным email уже зарегистрирован.");
        }
    }
}
