package ru.devtrifanya.online_store.rest.dto.entities_dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CartElementDTO {
    private int id;

    @Min(value = 1, message = "В корзине не может быть меньше одного товара")
    private int itemCount;
}
