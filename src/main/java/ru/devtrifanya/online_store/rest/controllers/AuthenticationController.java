package ru.devtrifanya.online_store.rest.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.devtrifanya.online_store.rest.dto.entities_dto.UserDTO;
import ru.devtrifanya.online_store.rest.dto.requests.SignUpRequest;
import ru.devtrifanya.online_store.rest.dto.requests.SignInRequest;
import ru.devtrifanya.online_store.rest.dto.responses.JwtResponse;
import ru.devtrifanya.online_store.rest.utils.MainClassConverter;
import ru.devtrifanya.online_store.rest.validators.AuthenticationValidator;
import ru.devtrifanya.online_store.services.implementations.AuthenticationService;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    private final AuthenticationValidator authenticationValidator;

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
