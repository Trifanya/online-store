package ru.devtrifanya.online_store.controllers;

import jakarta.validation.Valid;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.dto.UserDTO;
import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.services.AuthService;
import ru.devtrifanya.online_store.services.UserService;
import ru.devtrifanya.online_store.util.errorResponses.ErrorResponse;
import ru.devtrifanya.online_store.util.exceptions.user.InvalidPersonDataException;
import ru.devtrifanya.online_store.util.exceptions.user.UserNotFoundException;
import ru.devtrifanya.online_store.util.validators.RegistrationValidator;

import java.util.List;

@RestController
@RequestMapping("/people")
@Data
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final RegistrationValidator registrationValidator;
    private final AuthService authService;

    @PatchMapping("/{id}")
    public ResponseEntity<String> edit(@RequestBody @Valid UserDTO userDTO,
                                           @PathVariable("id") int id,
                                           BindingResult bindingResult) {
        registrationValidator.validate(userDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            StringBuilder errorMessage = new StringBuilder();
            for (FieldError error : errors) {
                errorMessage.append(error.getDefaultMessage() + "\n");
            }
            throw new InvalidPersonDataException(errorMessage.toString());
        }
        userService.update(id, convertToPerson(userDTO));
        return ResponseEntity.ok("Данные пользователя изменены успешно.");
    }

    public User convertToPerson(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(UserNotFoundException exception) {
        ErrorResponse response = new ErrorResponse(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(InvalidPersonDataException exception) {
        ErrorResponse response = new ErrorResponse(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
