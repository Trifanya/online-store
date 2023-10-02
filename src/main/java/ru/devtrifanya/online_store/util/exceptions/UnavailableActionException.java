package ru.devtrifanya.online_store.util.exceptions;

public class UnavailableActionException extends RuntimeException {
    public UnavailableActionException(String message) {
        super(message);
    }
}
