package ru.devtrifanya.online_store.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "person")
public class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @NotEmpty(message = "Вы не указали имя.")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Имя должно состоять только из букв")
    private String name;

    @Column(name = "surname")
    @NotEmpty(message = "Вы не указали фамилию.")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Фамилия должна состоять только из букв")
    private String surname;

    @Column(name = "email")
    @NotEmpty(message = "Вы не указали свой email.")
    @Email(message = "Введите корректный email")
    private String email;

    @Column(name = "password")
    @NotEmpty(message = "Вы не ввели пароль.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#&()–[{}]():;',?/*~$^+=<>]).{8,}$",
            message = "Пароль должен содержать хотя бы 1 строчную букву, 1 прописную букву, 1 цифру и 1 служебный символ. \nДлина пароля должна быть не менее 8 символов. ")
    private String password;

    public Person() {

    }

    public Person(String name, String surname, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
