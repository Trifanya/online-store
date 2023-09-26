package ru.devtrifanya.online_store.controllers;

import jakarta.validation.Valid;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
import ru.devtrifanya.online_store.util.errorResponses.ErrorResponse;
import ru.devtrifanya.online_store.util.exceptions.user.InvalidPersonDataException;
import ru.devtrifanya.online_store.util.exceptions.user.UserAlreadyExistException;
import ru.devtrifanya.online_store.util.exceptions.user.UserNotFoundException;
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
            throw new InvalidPersonDataException(errorMessage.toString());
        }
        authService.signUpPerson(convertToPerson(userDTO));
        return ResponseEntity.ok("Регистрация прошла успешно.");
    }

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest,
                                             BindingResult bindingResult) {
        authenticationValidator.validate(authRequest, bindingResult);
        String jwt = authService.createAuthToken(authRequest);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    public User convertToPerson(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        //exception.printStackTrace();
        String errorMessage = null;
        HttpStatus status = null;
        if (exception instanceof BadCredentialsException) {
            errorMessage = "Вы ввели неверный пароль.";
            status = HttpStatus.UNAUTHORIZED;
        } else if (exception instanceof UserNotFoundException) {
            errorMessage = exception.getMessage();
            status = HttpStatus.NOT_FOUND;
        } else if (exception instanceof InvalidPersonDataException) {
            errorMessage = exception.getMessage();
            status = HttpStatus.BAD_REQUEST;
        } else if (exception instanceof UserAlreadyExistException) {
            errorMessage = exception.getMessage();
            status = HttpStatus.ALREADY_REPORTED;
        }
        ErrorResponse response = new ErrorResponse(errorMessage);
        return new ResponseEntity<>(response, status);
    }
}
