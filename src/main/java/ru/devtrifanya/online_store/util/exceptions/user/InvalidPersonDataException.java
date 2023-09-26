package ru.devtrifanya.online_store.util.exceptions.user;

public class InvalidPersonDataException extends RuntimeException {
    public InvalidPersonDataException(String message) {
        super(message);
    }
}
