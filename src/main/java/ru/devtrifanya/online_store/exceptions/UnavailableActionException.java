package ru.devtrifanya.online_store.exceptions;

public class UnavailableActionException extends RuntimeException {
    public UnavailableActionException(String message) {
        super(message);
    }
}
