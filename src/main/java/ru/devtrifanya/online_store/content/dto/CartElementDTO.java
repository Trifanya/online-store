package ru.devtrifanya.online_store.content.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CartElementDTO {
    @Min(value = 1, message = "В корзине не может быть меньше одного товара")
    private int itemCount;
}
