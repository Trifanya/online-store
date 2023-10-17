package ru.devtrifanya.online_store.rest.dto.entities_dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class ItemFeatureDTO {
    private int id;

    @NotBlank(message = "Необходимо указать значение характеристики товара.")
    private String stringValue;

}
