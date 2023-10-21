package ru.devtrifanya.online_store.rest.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import ru.devtrifanya.online_store.repositories.UserRepository;
import ru.devtrifanya.online_store.rest.dto.requests.SignInRequest;
import ru.devtrifanya.online_store.rest.dto.requests.SignUpRequest;
import ru.devtrifanya.online_store.exceptions.NotFoundException;
import ru.devtrifanya.online_store.exceptions.InvalidDataException;
import ru.devtrifanya.online_store.exceptions.AlreadyExistException;

@Component
@RequiredArgsConstructor
public class AuthenticationValidator {
    private final UserRepository userRepository;

    /**
     * Валидация запроса на регистрацию на сайте.
     */
    public void performSignUpValidation(SignUpRequest request) {
        validateUniqueEmail(request.getUser().getEmail());
        validatePasswordConfirmed(request.getUser().getPassword(), request.getPasswordConfirmation());
    }

    /**
     * Валидация запроса на вход в аккаунт.
     */
    public void performSignInValidation(SignInRequest request) {
        String email = request.getEmail();
        if (userRepository.findByEmail(email).isEmpty()) {
            throw new NotFoundException("Пользователь с указанным email не найден.");
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
