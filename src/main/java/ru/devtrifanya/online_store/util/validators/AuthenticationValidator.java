package ru.devtrifanya.online_store.util.validators;


import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.devtrifanya.online_store.repositories.UserRepository;
import ru.devtrifanya.online_store.security.jwt.JwtRequest;
import ru.devtrifanya.online_store.util.exceptions.user.UserNotFoundException;

@Component
@Data
public class AuthenticationValidator implements Validator {
    private final UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        JwtRequest request = (JwtRequest) target;
        if (userRepository.findByEmail(request.getEmail()).isEmpty()) {
            throw new UserNotFoundException();
        }
    }
}
