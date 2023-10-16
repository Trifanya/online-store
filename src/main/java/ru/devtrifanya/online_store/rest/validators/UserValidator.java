package ru.devtrifanya.online_store.rest.validators;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.repositories.UserRepository;
import ru.devtrifanya.online_store.rest.dto.entities_dto.UserDTO;
import ru.devtrifanya.online_store.exceptions.AlreadyExistException;

@Component
@Data
public class UserValidator {
    private final UserRepository userRepository;

    public void validate(UserDTO user, int userId) {
        User user1 = userRepository.findByEmail(user.getEmail()).orElse(null);
        if (user1 != null && user1.getId() != userId) {
            throw new AlreadyExistException("Пользователь с указанным email уже зарегистрирован.");
        }
    }
}
