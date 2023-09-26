package ru.devtrifanya.online_store.util.exceptions.user;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("Пользователь с указанным email не найден.");
    }
}
