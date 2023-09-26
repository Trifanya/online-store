package ru.devtrifanya.online_store.util.errorResponses;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ItemErrorResponse {
    public String message;
    public LocalDateTime timestamp;

    public ItemErrorResponse(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

}
