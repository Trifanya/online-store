package ru.devtrifanya.online_store.rest.dto.entities_dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ItemDTO {
    private int id;

    @NotBlank(message = "Необходимо указать название товара.")
    private String name;

    @NotBlank(message = "Необходимо указать производителя товара.")
    private String manufacturer;

    @NotNull(message = "Необходимо указать цену товара.")
    @Min(value = 0, message = "Цена не может быть ниже 0 руб.")
    private double price;

    @NotNull(message = "Необходимо указать количество товара в наличии.")
    private int quantity;

    @NotBlank(message = "Необходимо указать описание товара.")
    private String description;

    private double rating;
}
