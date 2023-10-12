package ru.devtrifanya.online_store.rest.controllers;

import jakarta.validation.Valid;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.devtrifanya.online_store.rest.dto.entities_dto.UserDTO;
import ru.devtrifanya.online_store.rest.dto.requests.SignUpRequest;
import ru.devtrifanya.online_store.rest.dto.requests.SignInRequest;
import ru.devtrifanya.online_store.rest.dto.responses.ErrorResponse;
import ru.devtrifanya.online_store.rest.dto.responses.JwtResponse;
import ru.devtrifanya.online_store.services.AuthenticationService;
import ru.devtrifanya.online_store.security.jwt.JWTUtils;
import ru.devtrifanya.online_store.util.MainClassConverter;
import ru.devtrifanya.online_store.util.MainExceptionHandler;
import ru.devtrifanya.online_store.rest.validators.AuthenticationValidator;

@RestController
@Data
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final AuthenticationService authenticationService;
    private final JWTUtils JWTUtils;
    private final ModelMapper modelMapper;
    private final AuthenticationValidator authenticationValidator;
    private final MainExceptionHandler exceptionHandler;
    private final MainClassConverter converter;

    @PostMapping("/registration")
    public ResponseEntity<UserDTO> signUp(@RequestBody @Valid SignUpRequest signUpRequest,
                                          BindingResult bindingResult) {
        authenticationValidator.validate(signUpRequest);
        if (bindingResult.hasErrors()) {
            exceptionHandler.throwInvalidDataException(bindingResult);
        }
        UserDTO userDTO = converter.convertToUserDTO(
                authenticationService.createNewUser(
                        converter.convertToUser(signUpRequest.getUser())
                ));
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/authentication")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest signInRequest,
                                    BindingResult bindingResult) {
        authenticationValidator.validate(signInRequest);
        if (bindingResult.hasErrors()) {
            exceptionHandler.throwInvalidDataException(bindingResult);
        }
        String jwt = authenticationService.getJWT(signInRequest);

        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        return exceptionHandler.handleException(exception);
    }
}
