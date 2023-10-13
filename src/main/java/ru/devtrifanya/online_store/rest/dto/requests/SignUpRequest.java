package ru.devtrifanya.online_store.rest.dto.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.devtrifanya.online_store.rest.dto.entities_dto.UserDTO;

@Data
public class SignUpRequest {
    private @Valid UserDTO user;

    @NotBlank(message = "Для регистрации необходимо подтвердить пароль.")
    private String passwordConfirmation;
}
