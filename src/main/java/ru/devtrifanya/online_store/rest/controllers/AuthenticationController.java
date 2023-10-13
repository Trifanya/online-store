package ru.devtrifanya.online_store.rest.controllers;

import jakarta.validation.Valid;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.devtrifanya.online_store.rest.dto.entities_dto.UserDTO;
import ru.devtrifanya.online_store.rest.dto.requests.SignUpRequest;
import ru.devtrifanya.online_store.rest.dto.requests.SignInRequest;
import ru.devtrifanya.online_store.rest.dto.responses.JwtResponse;
import ru.devtrifanya.online_store.services.AuthenticationService;
import ru.devtrifanya.online_store.security.jwt.JWTUtils;
import ru.devtrifanya.online_store.rest.utils.MainClassConverter;
import ru.devtrifanya.online_store.rest.utils.MainExceptionHandler;
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
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        authenticationValidator.validate(signUpRequest);
        UserDTO userDTO = converter.convertToUserDTO(
                authenticationService.createNewUser(
                        converter.convertToUser(signUpRequest.getUser())
                ));
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/authentication")
    public ResponseEntity<?> signIn(@RequestBody @Valid SignInRequest signInRequest) {
        authenticationValidator.validate(signInRequest);
        String jwt = authenticationService.getJWT(signInRequest);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }
}
