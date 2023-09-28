package ru.devtrifanya.online_store.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import ru.devtrifanya.online_store.util.exceptions.AlreadyExistException;
import ru.devtrifanya.online_store.util.exceptions.InvalidDataException;
import ru.devtrifanya.online_store.util.exceptions.NotFoundException;

@Component
public class MainExceptionHandler {

    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        String errorMessage = null;
        HttpStatus status = null;

        if (exception instanceof InvalidDataException) {
            errorMessage = exception.getMessage();
            status = HttpStatus.UNAUTHORIZED;
        } else if (exception instanceof NotFoundException) {
            errorMessage = exception.getMessage();
            status = HttpStatus.NOT_FOUND;
        } else if (exception instanceof AlreadyExistException) {
            errorMessage = exception.getMessage();
            status = HttpStatus.ALREADY_REPORTED;
        } else if (exception instanceof BadCredentialsException) {
            errorMessage = "Вы ввели неверный пароль.";
            status = HttpStatus.UNAUTHORIZED;
        }  else {
            errorMessage = "Произошла какая-то ошибка.";
            status = HttpStatus.BAD_REQUEST;
        }
        ErrorResponse response = new ErrorResponse(errorMessage);
        return new ResponseEntity<>(response, status);
    }
}
