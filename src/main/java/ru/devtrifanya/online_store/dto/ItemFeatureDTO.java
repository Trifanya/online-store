package ru.devtrifanya.online_store.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import ru.devtrifanya.online_store.models.Feature;

@Data
public class ItemFeatureDTO {
    //@NotEmpty(message = "Значение характеристики товара не должно быть пустым.")
    private String value;
}
