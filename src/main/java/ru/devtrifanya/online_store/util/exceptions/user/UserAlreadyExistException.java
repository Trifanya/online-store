package ru.devtrifanya.online_store.util.exceptions.user;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException() {
        super("Пользователь с указанным email уже существует.");
    }
}
