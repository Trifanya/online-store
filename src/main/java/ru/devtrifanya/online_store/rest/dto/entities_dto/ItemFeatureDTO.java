package ru.devtrifanya.online_store.rest.dto.entities_dto;

import lombok.Data;

@Data
public class ItemFeatureDTO {
    private int id;

    //@NotEmpty(message = "Значение характеристики товара не должно быть пустым.")
    private String stringValue;

}
