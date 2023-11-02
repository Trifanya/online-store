package ru.devtrifanya.online_store.rest.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import ru.devtrifanya.online_store.repositories.UserRepository;
import ru.devtrifanya.online_store.exceptions.NotFoundException;
import ru.devtrifanya.online_store.rest.dto.requests.SignInRequest;

@Component
@RequiredArgsConstructor
public class AuthenticationValidator {
    private final UserRepository userRepository;

    /**
     * Валидация запроса на вход в аккаунт.
     */
    public void performSignInValidation(SignInRequest request) {
        String email = request.getEmail();
        if (userRepository.findByEmail(email).isEmpty()) {
            throw new NotFoundException("Пользователь с указанным email не найден.");
        }
    }

}
