package ru.devtrifanya.online_store.util.errorResponses;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PersonErrorResponse {
    private String message;
    private LocalDateTime timestamp;

    public PersonErrorResponse(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

}
