package ru.devtrifanya.online_store.content.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserDTO {
    @NotEmpty(message = "Вы не указали имя.")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Имя должно состоять только из букв.")
    private String name;

    @NotEmpty(message = "Вы не указали фамилию.")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Фамилия должна состоять только из букв.")
    private String surname;

    @NotEmpty(message = "Вы не указали свой email.")
    @Email(message = "Введите корректный email.")
    private String email;

    @NotEmpty(message = "Вы не ввели пароль.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#&()–_[{}]():;',?/*~$^+=<>]).{8,}$",
             message = "Пароль должен содержать хотя бы 1 строчную букву, 1 прописную букву, 1 цифру и 1 служебный символ. \nМинимальная длина пароля - 8 символов. ")
    private String password;

    //@NotEmpty(message = "Вы не подтвердили пароль.")
    /*@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#&()–[{}]():;',?/*~$^+=<>]).{8,40}$",
            message = "Пароль должен содержать хотя бы 1 строчную букву, 1 прописную букву, 1 цифру и 1 служебный символ. \nМинимальная длина пароля - 8 символов. ")*/
    //private String passwordConfirmation;

}
