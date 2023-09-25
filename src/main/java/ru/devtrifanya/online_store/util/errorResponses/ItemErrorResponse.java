package ru.devtrifanya.online_store.util.errorResponses;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

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
