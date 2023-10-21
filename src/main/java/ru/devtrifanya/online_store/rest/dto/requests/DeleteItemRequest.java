package ru.devtrifanya.online_store.rest.dto.requests;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class DeleteItemRequest {
    @Positive(message = "id товара должен быть положительным")
    private int itemToDeleteId;
}
