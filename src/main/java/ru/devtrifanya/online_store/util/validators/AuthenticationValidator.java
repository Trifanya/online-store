package ru.devtrifanya.online_store.util.validators;


import lombok.Data;
import org.springframework.stereotype.Component;
import ru.devtrifanya.online_store.repositories.UserRepository;
import ru.devtrifanya.online_store.security.jwt.JwtRequest;
import ru.devtrifanya.online_store.util.exceptions.NotFoundException;

@Component
@Data
public class AuthenticationValidator {
    private final UserRepository userRepository;

    public void validate(JwtRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isEmpty()) {
            throw new NotFoundException("Пользователь с указанным email не найден.");
        }
    }
}
