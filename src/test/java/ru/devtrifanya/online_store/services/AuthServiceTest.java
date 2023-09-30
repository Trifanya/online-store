package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.devtrifanya.online_store.repositories.UserRepository;
import ru.devtrifanya.online_store.security.jwt.JWTUtils;

@ExtendWith(MockitoExtension.class)
@Data
public class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JWTUtils JWTUtils;

    @InjectMocks
    private AuthService authService;

    @Test
    public void create_shouldSaveUser() {

    }

    @Test
    public void createAuthToken_passwordIsCorrect_shouldReturnJWT() {

    }

    @Test
    public void createAuthToken_passwordIsIncorrect_shouldThrowBadCredentialsException() {

    }
}
