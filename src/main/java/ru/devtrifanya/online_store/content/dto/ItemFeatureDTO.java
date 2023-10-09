package ru.devtrifanya.online_store.content.dto;

import lombok.Data;

@Data
public class ItemFeatureDTO {
    //@NotEmpty(message = "Значение характеристики товара не должно быть пустым.")
    private String value;

}
