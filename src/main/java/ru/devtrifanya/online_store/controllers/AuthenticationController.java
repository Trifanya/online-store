package ru.devtrifanya.online_store.controllers;

import jakarta.validation.Valid;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
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
import ru.devtrifanya.online_store.services.AuthenticationService;
import ru.devtrifanya.online_store.security.jwt.JwtRequest;
import ru.devtrifanya.online_store.security.jwt.JwtResponse;
import ru.devtrifanya.online_store.security.jwt.JWTUtils;
import ru.devtrifanya.online_store.services.UserService;
import ru.devtrifanya.online_store.util.errorResponses.AuthenticationErrorResponse;
import ru.devtrifanya.online_store.util.exceptions.person.InvalidPersonDataException;
import ru.devtrifanya.online_store.util.validators.UserValidator;

import java.util.List;

@RestController
@Data
public class AuthenticationController {
   private final UserValidator userValidator;
   private final AuthenticationManager authenticationManager;
   private final JWTUtils JWTUtils;
   private final UserService userService;
   private final ModelMapper modelMapper;
   private final AuthenticationService authenticationService;

    @PostMapping("/authentication")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        String jwt = authenticationService.createAuthToken(authRequest);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> signUp(@RequestBody @Valid UserDTO userDTO,
                                             BindingResult bindingResult) {
        userValidator.validate(userDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            StringBuilder errorMessage = new StringBuilder();
            for (FieldError error : errors) {
                errorMessage
                        .append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }
            throw new InvalidPersonDataException(errorMessage.toString());
        }
        authenticationService.signUpPerson(convertToPerson(userDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    public User convertToPerson(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    @ExceptionHandler
    public ResponseEntity<AuthenticationErrorResponse> handleException(Exception exception) {
        AuthenticationErrorResponse response = new AuthenticationErrorResponse("Проблемы при входе или регистрации.");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}
