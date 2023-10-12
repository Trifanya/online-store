package ru.devtrifanya.online_store.util;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.devtrifanya.online_store.rest.dto.responses.ErrorResponse;
import ru.devtrifanya.online_store.util.exceptions.AlreadyExistException;
import ru.devtrifanya.online_store.util.exceptions.InvalidDataException;
import ru.devtrifanya.online_store.util.exceptions.NotFoundException;
import ru.devtrifanya.online_store.util.exceptions.UnavailableActionException;

import java.util.List;

@Component
public class MainExceptionHandler {

    public void throwInvalidDataException(BindingResult bindingResult) {
        List<FieldError> errors = bindingResult.getFieldErrors();
        StringBuilder errorMessage = new StringBuilder();
        for (FieldError error : errors) {
            errorMessage.append(error.getDefaultMessage() + "\n");
        }
        throw new InvalidDataException(errorMessage.toString());
    }

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
        } else if (exception instanceof UnavailableActionException) {
            errorMessage = exception.getMessage();
            status = HttpStatus.BAD_REQUEST;
        } else if (exception instanceof ConstraintViolationException) {
            errorMessage = exception.getMessage();
            status = HttpStatus.BAD_REQUEST;

        } else if (exception instanceof BadCredentialsException) {
            errorMessage = "Вы ввели неверный пароль.";
            status = HttpStatus.UNAUTHORIZED;
        } else {
            errorMessage = "Произошла какая-то ошибка.";
            exception.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
        }
        ErrorResponse response = new ErrorResponse(errorMessage);
        return new ResponseEntity<>(response, status);
    }
}
