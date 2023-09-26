package ru.devtrifanya.online_store.util.exceptions.item;

public class ItemAlreadyExistException extends RuntimeException {
    public ItemAlreadyExistException() {
        super("Товар с таким названием уже существует.");
    }
}
