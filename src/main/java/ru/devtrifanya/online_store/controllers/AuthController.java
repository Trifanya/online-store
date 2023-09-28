package ru.devtrifanya.online_store.controllers;

import jakarta.validation.Valid;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.devtrifanya.online_store.dto.UserDTO;
import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.services.AuthService;
import ru.devtrifanya.online_store.security.jwt.JwtRequest;
import ru.devtrifanya.online_store.security.jwt.JwtResponse;
import ru.devtrifanya.online_store.security.jwt.JWTUtils;
import ru.devtrifanya.online_store.util.ErrorResponse;
import ru.devtrifanya.online_store.util.MainExceptionHandler;
import ru.devtrifanya.online_store.util.exceptions.InvalidDataException;
import ru.devtrifanya.online_store.util.validators.AuthenticationValidator;
import ru.devtrifanya.online_store.util.validators.RegistrationValidator;

import java.util.List;

@RestController
@Data
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final JWTUtils JWTUtils;
    private final ModelMapper modelMapper;
    private final RegistrationValidator registrationValidator;
    private final AuthenticationValidator authenticationValidator;
    private final MainExceptionHandler mainExceptionHandler;

    @PostMapping("/registration")
    public ResponseEntity<String> signUp(@RequestBody @Valid UserDTO userDTO,
                                             BindingResult bindingResult) {
        registrationValidator.validate(userDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            StringBuilder errorMessage = new StringBuilder();
            for (FieldError error : errors) {
                errorMessage.append(error.getDefaultMessage() + "\n");
            }
            throw new InvalidDataException(errorMessage.toString());
        }
        authService.create(convertToUser(userDTO));
        return ResponseEntity.ok("Регистрация прошла успешно.");
    }

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest,
                                             BindingResult bindingResult) {
        authenticationValidator.validate(authRequest, bindingResult);
        String jwt = authService.createAuthToken(authRequest);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    public User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        return mainExceptionHandler.handleException(exception);
    }
}
