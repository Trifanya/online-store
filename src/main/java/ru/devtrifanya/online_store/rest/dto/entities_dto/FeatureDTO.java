package ru.devtrifanya.online_store.rest.dto.entities_dto;

import lombok.Data;

@Data
public class FeatureDTO {
    private int id;

    private String name;

    private String requestParamName;

    private String unit;
}
