package ru.devtrifanya.online_store.util.exceptions.item;

public class ItemAlreadyExistException extends RuntimeException {
    public ItemAlreadyExistException(String message) {
        super(message);
    }
}
