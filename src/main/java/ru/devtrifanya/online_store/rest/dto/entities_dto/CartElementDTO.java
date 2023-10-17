package ru.devtrifanya.online_store.rest.dto.entities_dto;

import lombok.Data;

import jakarta.validation.constraints.Min;

@Data
public class CartElementDTO {
    private int id;

    @Min(value = 1, message = "id товара не должен быть меньше 1")
    private int itemId;

    @Min(value = 1, message = "В корзине не может быть меньше одного товара")
    private int itemCount;
}
