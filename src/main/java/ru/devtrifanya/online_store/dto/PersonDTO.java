package ru.devtrifanya.online_store.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.Check;

public class PersonDTO {
    @NotEmpty(message = "Вы не указали имя.")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Имя должно состоять только из букв")
    private String name;

    @NotEmpty(message = "Вы не указали фамилию.")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Фамилия должна состоять только из букв")
    private String surname;

    @NotEmpty(message = "Вы не указали свой email.")
    @Email(message = "Введите корректный email.")
    private String email;

    @NotEmpty(message = "Вы не ввели пароль.")
    /*@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#&()–[{}]():;',?/*~$^+=<>]).{8,40}$",
             message = "Пароль должен содержать хотя бы 1 строчную букву, 1 прописную букву, 1 цифру и 1 служебный символ. \nМаксимальная длина пароля - 40 символов. ")*/
    private String password;

    @NotEmpty(message = "Вы не подтвердили пароль.")
/*    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#&()–[{}]():;',?/*~$^+=<>]).{8,40}$",
            message = "Пароль должен содержать хотя бы 1 строчную букву, 1 прописную букву, 1 цифру и 1 служебный символ. \nМаксимальная длина пароля - 40 символов. ")*/
    private String passwordConfirmation;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }
}
