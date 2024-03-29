package ru.devtrifanya.online_store.rest.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ru.devtrifanya.online_store.rest.dto.responses.JwtResponse;
import ru.devtrifanya.online_store.services.AuthenticationService;
import ru.devtrifanya.online_store.rest.dto.requests.SignInRequest;
import ru.devtrifanya.online_store.rest.validators.AuthenticationValidator;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    private final AuthenticationValidator authenticationValidator;

    /**
     * Вход в зарегистрированный аккаунт.
     */
    @PostMapping("/authentication")
    public ResponseEntity<?> signIn(@RequestBody @Valid SignInRequest signInRequest) {
        authenticationValidator.performSignInValidation(signInRequest);

        String jwt = authenticationService.getJWT(signInRequest);

        return ResponseEntity.ok(new JwtResponse(jwt));
    }
}
