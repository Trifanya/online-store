package ru.devtrifanya.online_store.rest.dto.requests;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class DeleteFromCartRequest {
    @Min(value = 1, message = "id элемента корзины должен быть не меньше 1")
    private int cartElementToDeleteId;
}
