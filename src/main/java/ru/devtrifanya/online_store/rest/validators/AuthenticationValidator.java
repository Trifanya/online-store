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

    public void validateSignUp(SignUpRequest request) {
        validateUniqueEmail(request.getUser().getEmail());
        validatePasswordConfirmed(request.getUser().getPassword(), request.getPasswordConfirmation());
    }

    public void validateUserIsExist(SignInRequest request) {
        String email = request.getEmail();
        if (userRepository.findByEmail(email).isEmpty()) {
            throw new NotFoundException("Пользователь с указанным email не найден.");
        }
    }

    public void validateUniqueEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new AlreadyExistException("Пользователь с указанным email уже зарегистрирован.");
        }
    }

    public void validatePasswordConfirmed(String password, String passwordConfirmation) {
        if (!password.equals(passwordConfirmation)) {
            throw new InvalidDataException("Пароли не совпадают.");
        }
    }

}
