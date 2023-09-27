package ru.devtrifanya.online_store.util.exceptions.review;

public class ReviewAlreadyPostedException extends RuntimeException {
    public ReviewAlreadyPostedException() {
        super("Вы уже опубликовали отзыв о данном товаре.");
    }
}
