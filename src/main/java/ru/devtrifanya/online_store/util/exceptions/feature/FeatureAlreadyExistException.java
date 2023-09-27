package ru.devtrifanya.online_store.util.exceptions.feature;

public class FeatureAlreadyExistException extends RuntimeException {
    public FeatureAlreadyExistException() {
        super("Характеристика с таким названием уже существует.");
    }
}
