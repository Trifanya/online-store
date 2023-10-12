package ru.devtrifanya.online_store.rest.dto.requests;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SignInRequest {
    @NotEmpty(message = "Вы не ввели email.")
    private String email;

    @NotEmpty(message = "Вы не ввели пароль.")
    private String password;
}
