package ru.devtrifanya.online_store.util.errorResponses;

import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

public class AuthenticationErrorResponse {
    private String message;
    private LocalDateTime timestamp;

    public AuthenticationErrorResponse(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
