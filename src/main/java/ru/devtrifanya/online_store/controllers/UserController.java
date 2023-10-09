package ru.devtrifanya.online_store.controllers;

import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.content.dto.UserDTO;
import ru.devtrifanya.online_store.services.AuthService;
import ru.devtrifanya.online_store.services.UserService;
import ru.devtrifanya.online_store.util.ErrorResponse;
import ru.devtrifanya.online_store.util.MainClassConverter;
import ru.devtrifanya.online_store.util.MainExceptionHandler;
import ru.devtrifanya.online_store.util.exceptions.NotFoundException;
import ru.devtrifanya.online_store.util.validators.RegistrationValidator;

@RestController
@RequestMapping("/profile")
@Data
public class UserController {
    private final UserService userService;
    private final RegistrationValidator registrationValidator;
    private final AuthService authService;
    private final MainExceptionHandler exceptionHandler;
    private final MainClassConverter converter;

    @PatchMapping("/edit/{userId}")
    public ResponseEntity<String> edit(@RequestBody @Valid UserDTO userDTO,
                                           @PathVariable("id") int id,
                                           BindingResult bindingResult) {
        registrationValidator.validate(userDTO);
        if (bindingResult.hasErrors()) {
            exceptionHandler.throwInvalidDataException(bindingResult);
        }
        userService.update(id, converter.convertToUser(userDTO));
        return ResponseEntity.ok("Ваши данные успешно изменены.");
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(NotFoundException exception) {
        return exceptionHandler.handleException(exception);
    }

}
