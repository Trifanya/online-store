package ru.devtrifanya.online_store.util.validators;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.devtrifanya.online_store.dto.UserDTO;
import ru.devtrifanya.online_store.services.UserService;
import ru.devtrifanya.online_store.util.exceptions.person.PersonAlreadyExistException;

@Component
@Data
public class UserValidator implements Validator {
    private final UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO userDTO = (UserDTO) target;

        if (userService.findOne(userDTO.getEmail()).isPresent()) {
            throw new PersonAlreadyExistException("Пользователь с таким email уже существует.");
        }
    }
}
