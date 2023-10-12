package ru.devtrifanya.online_store.rest.dto.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.devtrifanya.online_store.rest.dto.entities_dto.UserDTO;

@Data
public class SignUpRequest {
    @NotNull(message = "Для регистрации требуется ввести данные пользователя.")
    private UserDTO user;

    @NotEmpty(message = "Для регистрации требуется подтверждение пароля.")
    private String passwordConfirmation;
}
