package ru.devtrifanya.online_store.rest.dto.entities_dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class FeatureDTO {
    private int id;

    @NotBlank(message = "Название характеристики не должно быть пустым.")
    private String name;

    @NotBlank(message = "Псевдоним характеристики не должен быть пустым.")
    private String requestParamName;

    private String unit;
}
