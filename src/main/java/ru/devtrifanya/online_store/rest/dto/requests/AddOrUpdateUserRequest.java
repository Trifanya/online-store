package ru.devtrifanya.online_store.rest.dto.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import ru.devtrifanya.online_store.rest.dto.entities_dto.UserDTO;

@Data
public class AddOrUpdateUserRequest {
    @NotNull(message = "Данные пользователя должны быть указаны.")
    private @Valid UserDTO user;

    @NotBlank(message = "Для регистрации необходимо подтвердить пароль.")
    private String passwordConfirmation;
}
