package ru.devtrifanya.online_store.unit_tests.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.devtrifanya.online_store.models.User;
import ru.devtrifanya.online_store.services.UserService;
import ru.devtrifanya.online_store.repositories.UserRepository;
import ru.devtrifanya.online_store.exceptions.NotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private static final int USER_ID = 1;
    private static final String USER_ROLE = "ROLE_USER";
    private static final String USER_NAME = "Name1";
    private static final String USER_SURNAME = "Surname1";
    private static final String USER_EMAIL = "test1@mail.ru";
    private static final String USER_PASSWORD = "testPassword_1";
    private static final String ENCODED_PASSWORD = "encodedTestPassword_1";

    @Mock
    private PasswordEncoder passwordEncoderMock;
    @Mock
    private UserRepository userRepoMock;

    @InjectMocks
    private UserService testingService;

    @Test()
    public void loadUserByUsername_userIsExist_shouldReturnUser() {
        // Given
        mockFindByEmail_exist();

        // When
        User resultUser = testingService.loadUserByUsername(USER_EMAIL);

        // Then
        Mockito.verify(userRepoMock).findByEmail(USER_EMAIL);
        Assertions.assertEquals(getUser(USER_ID), resultUser);
    }

    @Test()
    public void loadUserByUsername_userIsNotExist_shouldThrowException() {
        // Given
        mockFindByEmail_notExist();

        // When // Then
        Assertions.assertThrows(
                NotFoundException.class,
                () -> testingService.loadUserByUsername(USER_EMAIL)
        );
    }

    @Test
    public void getUser_userIsExist_shouldReturnUser() {
        // Given
        mockFindById_exist();

        // When
        User resultUser = testingService.getUser(USER_ID);

        // Then
        Mockito.verify(userRepoMock).findById(USER_ID);
        Assertions.assertEquals(getUser(USER_ID), resultUser);
    }

    @Test
    public void getUser_userIsNotExist_shouldThrowException() {
        // Given
        mockFindById_notExist();

        // When // Then
        Assertions.assertThrows(NotFoundException.class, () -> testingService.getUser(USER_ID));
    }

    @Test
    public void createNewUser_shouldAssignIdEncryptedPasswordAndUserRole() {
        // Given
        User userToSave = getUser(0).setPassword(USER_PASSWORD);
        mockEncode();
        mockSaveNew();

        // When
        User resultUser = testingService.createNewUser(userToSave);

        // Then
        Mockito.verify(userRepoMock).save(userToSave);
        Mockito.verify(passwordEncoderMock).encode(USER_PASSWORD);
        Assertions.assertNotNull(resultUser.getId());
        Assertions.assertEquals(USER_ROLE, resultUser.getRole());
        Assertions.assertNotEquals(USER_PASSWORD, resultUser.getPassword());
    }

    @Test
    public void updateUser_shouldNotChangeIdAndRole() {
        // Given
        User updatedUser = getUser(USER_ID);
        User userToUpdate = getFoundUser(USER_ID);
        mockSaveUpdated();

        // When
        User resultUser = testingService.updateUser(userToUpdate, updatedUser);

        // Then
        Mockito.verify(userRepoMock).save(updatedUser);
        Assertions.assertEquals(userToUpdate.getId(), resultUser.getId());
        Assertions.assertEquals(userToUpdate.getRole(), resultUser.getRole());
    }

    @Test
    public void deleteUser_shouldInvokeDeleteMethod() {
        // When
        testingService.deleteUser(USER_ID);

        // Then
        Mockito.verify(userRepoMock).deleteById(USER_ID);
    }


    // Определение поведения mock-объектов

    private void mockFindById_exist() {
        Mockito.doAnswer(invocationOnMock -> Optional.of(getUser(invocationOnMock.getArgument(0))))
                .when(userRepoMock).findById(anyInt());
    }

    private void mockFindById_notExist() {
        Mockito.when(userRepoMock.findById(anyInt()))
                .thenReturn(Optional.empty());
    }

    private void mockFindByEmail_exist() {
        Mockito.doAnswer(invocationOnMock -> Optional.of(getUser(USER_ID)))
                .when(userRepoMock).findByEmail(anyString());
    }

    private void mockFindByEmail_notExist() {
        Mockito.when(userRepoMock.findByEmail(anyString()))
                .thenReturn(Optional.empty());
    }


    private void mockEncode() {
        Mockito.when(passwordEncoderMock.encode(USER_PASSWORD))
                .thenReturn(ENCODED_PASSWORD);
    }

    private void mockSaveNew() {
        Mockito.doAnswer(
                invocationOnMock -> {
                    User user = invocationOnMock.getArgument(0);
                    user.setId(USER_ID);
                    return user;
                }).when(userRepoMock).save(any(User.class));
    }

    private void mockSaveUpdated() {
        Mockito.doAnswer(invocationOnMock -> invocationOnMock.getArgument(0))
                .when(userRepoMock).save(any(User.class));
    }


    // Вспомогательные методы

    private User getUser(int userId) {
        return new User()
                .setId(userId);
    }

    private User getFoundUser(int userId) {
        return new User()
                .setId(userId)
                .setName(USER_NAME)
                .setSurname(USER_SURNAME)
                .setEmail(USER_EMAIL)
                .setPassword(USER_PASSWORD)
                .setRole(USER_ROLE);
    }
}
