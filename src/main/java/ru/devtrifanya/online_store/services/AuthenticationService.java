package ru.devtrifanya.online_store.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.security.jwt.JWTUtils;
import ru.devtrifanya.online_store.rest.dto.requests.SignInRequest;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JWTUtils JWTUtils;

    /**
     * Выдача JWT пользователю, выполнившему вход в аккаунт.
     */
    public String getJWT(SignInRequest signInRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.getEmail(),
                        signInRequest.getPassword()
                ));
        User user = (User) authentication.getPrincipal();
        return JWTUtils.generateToken(user);
    }
}
