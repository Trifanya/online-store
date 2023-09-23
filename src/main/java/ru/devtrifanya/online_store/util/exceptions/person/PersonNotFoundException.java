package ru.devtrifanya.online_store.util.exceptions.person;

public class PersonNotFoundException extends RuntimeException {
    public PersonNotFoundException(String message) {
        super(message);
    }
}
