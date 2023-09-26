package ru.devtrifanya.online_store.util.errorResponses;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserErrorResponse {
    private String message;
    private LocalDateTime timestamp;

    public UserErrorResponse(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

}
