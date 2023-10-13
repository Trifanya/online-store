package ru.devtrifanya.online_store.rest.dto.entities_dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserDTO {
    @NotBlank(message = "Необходимо указать имя.")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Имя должно состоять только из букв.")
    private String name;

    @NotEmpty(message = "Необходимо указать фамилию.")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Фамилия должна состоять только из букв.")
    private String surname;

    @NotBlank(message = "Необходимо указать email.")
    @Email(message = "Необходимо указать корректный email.")
    private String email;

    @NotEmpty(message = "Необходимо указать пароль.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#&()–_[{}]():;',?/*~$^+=<>]).{8,}$",
             message = "Пароль должен содержать хотя бы 1 строчную букву, 1 прописную букву, 1 цифру и 1 служебный символ. \n" +
                     "Минимальная длина пароля - 8 символов. ")
    private String password;
}
