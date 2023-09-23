package ru.devtrifanya.online_store.util.exceptions.person;

public class InvalidPersonDataException extends RuntimeException {
    public InvalidPersonDataException(String message) {
        super(message);
    }
}
