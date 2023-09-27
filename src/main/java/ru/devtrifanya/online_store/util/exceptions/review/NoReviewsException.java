package ru.devtrifanya.online_store.util.exceptions.review;

public class NoReviewsException extends RuntimeException {
    public NoReviewsException() {
        super("О данном товаре пока что нет ни одного отзыва.");
    }
}
