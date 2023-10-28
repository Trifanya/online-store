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

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private static final int USER_ID = 1;
    private static final String USER_ROLE = "ROLE_USER";
    private static final String USER_NAME = "Nikita";
    private static final String ANOTHER_USER_NAME = "Sergei";
    private static final String USER_EMAIL = "test@mail.ru";

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService testingService;

    @Test()
    public void loadUserByUsername_userIsExist_shouldReturnUser() {
        User expectedUser = user(USER_NAME);
        Mockito.when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(expectedUser));

        User resultUser = testingService.loadUserByUsername(USER_EMAIL);

        Mockito.verify(userRepository).findByEmail(USER_EMAIL);
        Assertions.assertEquals(expectedUser, resultUser);
    }

    @Test()
    public void loadUserByUsername_userIsNotExist_shouldThrowException() {
        Assertions.assertThrows(NotFoundException.class, () -> testingService.loadUserByUsername(USER_EMAIL));
    }

    @Test
    public void getUser_userIsExist_shouldReturnUser() {
        User expectedUser = user(USER_NAME);
        Mockito.when(userRepository.findById(USER_ID)).thenReturn(Optional.of(expectedUser));

        User resultUser = testingService.getUser(USER_ID);

        Mockito.verify(userRepository).findById(USER_ID);
        Assertions.assertEquals(expectedUser, resultUser);
    }

    @Test
    public void getUser_userIsNotExist_shouldThrowException() {
        Assertions.assertThrows(NotFoundException.class, () -> testingService.getUser(USER_ID));
    }

    @Test
    public void updateUserInfo_differentUserName_shouldAssignUpdatedUserId() {
        User user = user(USER_NAME);
        Mockito.when(userRepository.save(user)).thenReturn(user);

        User resultUser = testingService.updateUserInfo(user(ANOTHER_USER_NAME), user);

        Mockito.verify(userRepository).save(user);
        Assertions.assertEquals(user.getId(), resultUser.getId());
        Assertions.assertEquals(user.getRole(), resultUser.getRole());
        Assertions.assertEquals(user.getName(), resultUser.getName());
    }

    @Test
    public void deleteUser_shouldInvokeDeleteMethod() {
        testingService.deleteUser(USER_ID);

        Mockito.verify(userRepository).deleteById(USER_ID);
    }

    private User user(String name){
        return new User()
                .setId(USER_ID)
                .setRole(USER_ROLE)
                .setEmail(USER_EMAIL)
                .setName(name);
    }
}
