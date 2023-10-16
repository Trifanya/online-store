package ru.devtrifanya.online_store.rest.dto.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.devtrifanya.online_store.rest.dto.entities_dto.UserDTO;

@Data
public class UpdateUserRequest {
    @NotNull(message = "Данные пользователя должны быть указаны.")
    private @Valid UserDTO user;
    private String passwordConfirmation;
}
