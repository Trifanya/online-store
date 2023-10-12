package ru.devtrifanya.online_store.rest.dto.responses;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private String message;
    private LocalDateTime timestamp;

    public ErrorResponse(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
