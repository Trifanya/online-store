package ru.devtrifanya.online_store.rest.controllers;

import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.rest.dto.entities_dto.UserDTO;
import ru.devtrifanya.online_store.rest.dto.responses.ErrorResponse;
import ru.devtrifanya.online_store.rest.validators.UserValidator;
import ru.devtrifanya.online_store.services.AuthenticationService;
import ru.devtrifanya.online_store.services.UserService;
import ru.devtrifanya.online_store.util.MainClassConverter;
import ru.devtrifanya.online_store.util.MainExceptionHandler;
import ru.devtrifanya.online_store.util.exceptions.NotFoundException;

@RestController
@RequestMapping("/profile")
@Data
public class UserController {
    private final UserService userService;

    private final UserValidator userValidator;

    private final MainExceptionHandler exceptionHandler;

    private final MainClassConverter converter;

    @PatchMapping("/updateUserInfo")
    public ResponseEntity<UserDTO> updateUserInfo(@RequestBody @Valid UserDTO userDTO,
                                                 @AuthenticationPrincipal User user,
                                                 BindingResult bindingResult) {
        userValidator.validate(userDTO);
        if (bindingResult.hasErrors()) {
            exceptionHandler.throwInvalidDataException(bindingResult);
        }
        User updatedUser = userService.updateUserInfo(user.getId(), converter.convertToUser(userDTO));

        return ResponseEntity.ok(converter.convertToUserDTO(updatedUser));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(NotFoundException exception) {
        return exceptionHandler.handleException(exception);
    }

}
