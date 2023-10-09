package ru.devtrifanya.online_store.util.validators;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.devtrifanya.online_store.content.dto.UserDTO;
import ru.devtrifanya.online_store.repositories.UserRepository;
import ru.devtrifanya.online_store.util.exceptions.AlreadyExistException;

@Component
@Data
public class RegistrationValidator {
    private final UserRepository userRepository;

    public void validate(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new AlreadyExistException("Пользователь с указанным email уже зарегистрирован.");
        }
    }
}
