package ru.devtrifanya.online_store.rest.validators;


import lombok.Data;
import org.springframework.stereotype.Component;
import ru.devtrifanya.online_store.repositories.UserRepository;
import ru.devtrifanya.online_store.rest.dto.requests.SignInRequest;
import ru.devtrifanya.online_store.rest.dto.requests.SignUpRequest;
import ru.devtrifanya.online_store.util.exceptions.AlreadyExistException;
import ru.devtrifanya.online_store.util.exceptions.InvalidDataException;
import ru.devtrifanya.online_store.util.exceptions.NotFoundException;

@Component
@Data
public class AuthenticationValidator {
    private final UserRepository userRepository;

    public void validate(SignUpRequest signUpRequest) {
        String password = signUpRequest.getUser().getPassword();
        String passwordConfirmation = signUpRequest.getPasswordConfirmation();
        if (!password.equals(passwordConfirmation)) {
            throw new InvalidDataException("Пароли не совпадают.");
        }
    }

    public void validate(SignInRequest signInRequest) {
        String email = signInRequest.getEmail();
        if (userRepository.findByEmail(email).isEmpty()) {
            throw new NotFoundException("Пользователь с указанным email не найден.");
        }
    }
}
