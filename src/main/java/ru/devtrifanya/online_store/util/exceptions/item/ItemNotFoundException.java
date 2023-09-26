package ru.devtrifanya.online_store.util.exceptions.item;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException() {
        super("Товар с таким названием не найден.");
    }
}
