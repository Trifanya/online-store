package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.devtrifanya.online_store.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
@Data
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void loadUserByUsername_userIsExist_shouldReturnUserByEmail() {
        // given

        // when

        // then
    }

    @Test
    public void loadUserByUsername_userIsNotExist_shouldThrowNotFoundException() {
        // given

        // when

        // then
    }

    @Test
    public void update_shouldUpdateUserById() {
        // given

        // when

        // then
    }

    @Test
    public void delete_shouldDeleteUserById() {
        // given

        // when

        // then
    }
}
