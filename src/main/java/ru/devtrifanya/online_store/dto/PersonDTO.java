package ru.devtrifanya.online_store.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class PersonDTO {
    @NotEmpty(message = "Вы не указали имя.")
    private String name;

    @NotEmpty(message = "Вы не указали фамилию.")
    private String surname;

    @NotEmpty(message = "Вы не указали свой email.")
    private String email;

    @NotEmpty(message = "Вы не ввели пароль.")
    @Size(min = 6, message = "Пароль должен содержать не менее 6 символов.")
    private String password;

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
}
