package ru.devtrifanya.online_store.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ItemDTO {
    @NotEmpty(message = "Вы не указали название товара.")
    private String name;

    @NotEmpty(message = "Вы не указали цену товара.")
    @Min(value = 0, message = "Цена не может быть ниже 0 руб.")
    private double price;

    @NotEmpty(message = "Вы не добавили описание товара.")
    private String description;

    private String imageURL;

    @NotEmpty(message = "Вы не указали количество товара.")
    private int quantity;

    @NotEmpty(message = "Вы не указали категорию товара.")
    private String category;

    @NotEmpty(message = "Вы не указали родительскую категорию товара.")
    private String parent_category;

}
