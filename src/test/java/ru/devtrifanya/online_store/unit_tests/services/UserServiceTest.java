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
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService testingService;

    private int userId = 1;
    private int updatedUserId = 2;
    private int userToDeleteId = 3;

    private String userEmail = "test@mail.ru";
    private String updatedUserEmail = "updated@mail.ru";

    private User foundUser = new User(userId, "name1", "surname1", userEmail, "ROLE_USER");
    private User updatedUser = new User(updatedUserId, "name2", "surname2", updatedUserEmail, null);

    @Test
    public void loadUserByUsername_userIsExistShouldReturnUser() {
        // Определение поведения mock-объектов
        Mockito.when(userRepository.findByEmail(userEmail))
                .thenReturn(Optional.of(foundUser));

        // Выполнение тестируемого метода
        User resultUser = testingService.loadUserByUsername(userEmail);

        // Проверка совпадения ожидаемых вызовов методов с реальными
        Mockito.verify(userRepository).findByEmail(userEmail);
        Assertions.assertEquals(foundUser, resultUser);
    }

    @Test
    public void loadUserByUsername_userIsNotExist_shouldThrowException() {
        // Определение поведения mock-объектов
        Mockito.when(userRepository.findByEmail(userEmail))
                        .thenReturn(Optional.empty());
        // Выполнение тестируемого метода и проверка совпадения ожидаемого результата с реальным
        Assertions.assertThrows(
                NotFoundException.class,
                () -> testingService.loadUserByUsername(userEmail)
        );
        Mockito.verify(userRepository).findByEmail(userEmail);
    }

    @Test
    public void getUser_userIsExist_shouldReturnUser() {
        // Определение поведения mock-объектов
        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.of(foundUser));

        // Выполнение тестируемого метода
        User resultUser = testingService.getUser(userId);

        // Проверка совпадения ожидаемых вызовов методов с реальными
        Mockito.verify(userRepository).findById(userId);
        Assertions.assertEquals(foundUser, resultUser);
    }

    @Test
    public void getUser_userIsNotExist_shouldThrowException() {
        // Определение поведения mock-объектов
        Mockito.when(userRepository.findById(userId))
                        .thenReturn(Optional.empty());

        // Выполнение тестируемого метода и проверка совпадения ожидаемого результата с реальным
        Assertions.assertThrows(
                NotFoundException.class,
                () -> testingService.getUser(userId)
        );
        Mockito.verify(userRepository).findById(userId);
    }

    @Test
    public void updateUserInfo_shouldAssignUpdatedUserId() {
        // Определение поведения mock-объектов
        Mockito.when(userRepository.save(updatedUser))
                .thenReturn(updatedUser);

        // Выполнение тестируемого метода
        User resultUser = testingService.updateUserInfo(foundUser, updatedUser);

        // Проверка совпадения ожидаемых вызовов методов с реальными
        Mockito.verify(userRepository).save(updatedUser);
        Assertions.assertEquals(foundUser.getId(), resultUser.getId());
    }

    @Test
    public void updateUserInfo_shouldAssignUpdatedUserRole() {
        // Определение поведения mock-объектов
        Mockito.when(userRepository.save(updatedUser))
                .thenReturn(updatedUser);

        // Выполнение тестируемого метода
        User resultUser = testingService.updateUserInfo(foundUser, updatedUser);

        // Проверка совпадения ожидаемых вызовов методов с реальными
        Mockito.verify(userRepository).save(updatedUser);
        Assertions.assertEquals(foundUser.getRole(), resultUser.getRole());
    }

    @Test
    public void deleteUser_shouldInvokeDeleteMethod() {
        // Выполнение тестируемого метода
        testingService.deleteUser(userToDeleteId);

        // Проверка совпадения ожидаемых вызовов методов с реальными
        Mockito.verify(userRepository).deleteById(userToDeleteId);
    }
}
