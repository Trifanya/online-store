package ru.devtrifanya.online_store.unit_tests.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.repositories.UserRepository;
import ru.devtrifanya.online_store.rest.dto.requests.SignInRequest;
import ru.devtrifanya.online_store.security.jwt.JWTUtils;
import ru.devtrifanya.online_store.services.AuthenticationService;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    @Mock
    private AuthenticationManager authManagerMock;
    @Mock
    private PasswordEncoder passwordEncoderMock;
    @Mock
    private JWTUtils jwtUtilsMock;
    @Mock
    private UserRepository userRepoMock;

    @InjectMocks
    private AuthenticationService testingService;

    private int savedUserId = 1;

    private final String ROLE_USER = "ROLE_USER";
    private final String ROLE_ADMIN = "ROLE_ADMIN";

    private String email = "email1@mail.ru";
    private String decryptedPassword = "password_1";

    private String jwt = "data.payload.secret";

    private User userToSave = new User(
            0, "name1", "surname1",
            email, decryptedPassword,
            null, new ArrayList<>(), new ArrayList<>()
    );
    private User savedUser = new User(
            savedUserId, "name1", "surname1",
            email, decryptedPassword,
            null, new ArrayList<>(), new ArrayList<>()
    );

    private Authentication authentication = new UsernamePasswordAuthenticationToken(
            savedUser,
            decryptedPassword
    );

    private SignInRequest request = new SignInRequest("email1@mail.ru", "password_1");

    @Test
    public void createNewUser_shouldAssignEncryptedPassword() {
        createNewUser_determineBehaviorOfMocks();

        User resultUser = testingService.createNewUser(userToSave);

        Mockito.verify(passwordEncoderMock).encode(decryptedPassword);
        Assertions.assertNotEquals(decryptedPassword, resultUser.getPassword());
    }

    @Test
    public void createNewUser_shouldAssignUserRole() {
        createNewUser_shouldAssignEncryptedPassword();

        User resultUser = testingService.createNewUser(userToSave);
        Assertions.assertEquals(ROLE_USER, resultUser.getRole());
    }

    @Test
    public void createNewUser_shouldAssignId() {
        createNewUser_determineBehaviorOfMocks();

        User resultUser = testingService.createNewUser(userToSave);

        Mockito.verify(userRepoMock).save(userToSave);
        Assertions.assertNotNull(resultUser.getId());
    }

    @Test
    public void getJWT_shouldReturnJWT() {
        Mockito.when(authManagerMock.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                ))).thenReturn(authentication);
        Mockito.when(jwtUtilsMock.generateToken(savedUser))
                .thenReturn(jwt);

        String resultJWT = testingService.getJWT(request);

        Mockito.verify(authManagerMock).authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                ));
        Mockito.verify(jwtUtilsMock).generateToken(savedUser);
        Assertions.assertEquals(jwt, resultJWT);
    }


    public void createNewUser_determineBehaviorOfMocks() {
        Mockito.when(passwordEncoderMock.encode(decryptedPassword))
                .thenReturn("$2a$10$w/7n6pxybx.LQ0tqv8UYQ.OZ.W3FUDfkoB5lsLBTzCIXlzEt69mcK");
        Mockito.doAnswer(
                invocationOnMock -> {
                    User user = invocationOnMock.getArgument(0);
                    user.setId(savedUserId);
                    return user;
                }).when(userRepoMock).save(userToSave);
    }
}
