package ru.devtrifanya.online_store.unit_tests.services;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.security.jwt.JWTUtils;
import ru.devtrifanya.online_store.services.AuthenticationService;
import ru.devtrifanya.online_store.rest.dto.requests.SignInRequest;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    private static final String EMAIL = "email1@mail.ru";
    private static final String PASSWORD = "password_1";
    private static final String JWT = "metadata.payload.secret";

    @Mock
    private JWTUtils jwtUtilsMock;
    @Mock
    private AuthenticationManager authManagerMock;

    @InjectMocks
    private AuthenticationService testingService;

    @Test
    public void getJWT_shouldReturnJWT() {
        Authentication auth = getAuthentication();
        Mockito.when(authManagerMock.authenticate(new UsernamePasswordAuthenticationToken(EMAIL, PASSWORD)))
                .thenReturn(auth);
        Mockito.when(jwtUtilsMock.generateToken(any(User.class)))
                .thenReturn(JWT);

        String resultJWT = testingService.getJWT(getRequest());

        Mockito.verify(authManagerMock).authenticate(new UsernamePasswordAuthenticationToken(EMAIL, PASSWORD));
        Mockito.verify(jwtUtilsMock).generateToken(any(User.class));
        Assertions.assertEquals(JWT, resultJWT);
    }

    // Вспомогательные методы.

    private Authentication getAuthentication() {
        return new UsernamePasswordAuthenticationToken(getUser(), PASSWORD);
    }

    private User getUser() {
        return new User()
                .setEmail(EMAIL);
    }

    private SignInRequest getRequest() {
        return new SignInRequest()
                .setEmail(EMAIL)
                .setPassword(PASSWORD);
    }
}
