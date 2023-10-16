package ru.devtrifanya.online_store.rest.utils;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.devtrifanya.online_store.exceptions.*;
import ru.devtrifanya.online_store.rest.dto.responses.ErrorResponse;

import java.util.List;


@ControllerAdvice
public class MainExceptionHandler {

    @ExceptionHandler({InvalidDataException.class, UnavailableActionException.class, OutOfStockException.class})
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        String errorMessage = exception.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse response = new ErrorResponse(errorMessage);
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(NotFoundException exception) {
        String errorMessage = exception.getMessage();
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse response = new ErrorResponse(errorMessage);
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleException(AlreadyExistException exception) {
        String errorMessage = exception.getMessage();
        HttpStatus status = HttpStatus.ALREADY_REPORTED;
        ErrorResponse response = new ErrorResponse(errorMessage);
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleException(HttpMessageNotReadableException exception) {
        exception.printStackTrace();
        String errorMessage = "Некорректное тело запроса.";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse response = new ErrorResponse(errorMessage);
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleException(BadCredentialsException exception) {
        String errorMessage = "Вы ввели неверный пароль.";
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ErrorResponse response = new ErrorResponse(errorMessage);
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleException(ConstraintViolationException exception) {
        String errorMessage = exception.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse response = new ErrorResponse(errorMessage);
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException exception) {
        List<FieldError> errors = exception.getFieldErrors();
        StringBuilder message = new StringBuilder();
        for (FieldError error : errors) {
            message.append(error.getDefaultMessage() + "\n");
        }
        String errorMessage = message.toString();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse response = new ErrorResponse(errorMessage);
        return new ResponseEntity<>(response, status);
    }
}
