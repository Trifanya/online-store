package ru.devtrifanya.online_store.util.exceptions.person;

public class PersonAlreadyExistException extends RuntimeException {
    public PersonAlreadyExistException(String message) {
        super(message);
    }
}
