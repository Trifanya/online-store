package ru.devtrifanya.online_store.rest.validators;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.repositories.UserRepository;
import ru.devtrifanya.online_store.rest.dto.entities_dto.UserDTO;
import ru.devtrifanya.online_store.exceptions.InvalidDataException;
import ru.devtrifanya.online_store.exceptions.AlreadyExistException;
import ru.devtrifanya.online_store.rest.dto.requests.AddOrUpdateUserRequest;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository userRepository;

    /**
     * Валидация запроса на регистрацию на сайте.
     */
    public void performNewUserValidation(AddOrUpdateUserRequest request) {
        validateUniqueEmail(request.getUser().getEmail());
        validatePasswordConfirmed(request.getUser().getPassword(), request.getPasswordConfirmation());
    }

    /**
     * Валидация email пользователя.
     * Если в БД уже есть пользователь с указанным email, то будет выброшено исключение.
     */
    public void performUpdatedUserValidation(UserDTO user, int userId) {
        User namesake = userRepository.findByEmail(user.getEmail()).orElse(null);
        if (namesake != null && namesake.getId() != userId) {
            throw new AlreadyExistException("Пользователь с указанным email уже зарегистрирован.");
        }
    }

    /**
     * Валидация email пользователя.
     * Если в БД уже есть пользователь с указанным email, то будет выброшено исключение.
     */
    private void validateUniqueEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new AlreadyExistException("Пользователь с указанным email уже зарегистрирован.");
        }
    }

    /**
     * Валидация пароля пользователя.
     * Если подтверждение пароля не совпадает с основным паролем, то будет выброшено
     * исключение.
     */
    private void validatePasswordConfirmed(String password, String passwordConfirmation) {
        if (!password.equals(passwordConfirmation)) {
            throw new InvalidDataException("Пароли не совпадают.");
        }
    }
}
