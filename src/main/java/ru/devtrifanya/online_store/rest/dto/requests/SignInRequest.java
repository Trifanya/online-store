package ru.devtrifanya.online_store.rest.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SignInRequest {
    @Email(message = "Необходимо указать корректный email.")
    @NotBlank(message = "Для входа в аккаунт необходимо ввести email.")
    private String email;

    @NotBlank(message = "Для входа в аккаунт необходимо ввести пароль.")
    private String password;
}
