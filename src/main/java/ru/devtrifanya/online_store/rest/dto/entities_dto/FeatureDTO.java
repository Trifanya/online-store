package ru.devtrifanya.online_store.rest.dto.entities_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class FeatureDTO {
    private int id;

    @NotBlank(message = "Название характеристики не должно быть пустым.")
    private String name;

    @NotBlank(message = "Псевдоним характеристики не должен быть пустым.")
    @Pattern(regexp = "^+[a-z](?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{3,20}$",
            message = "Псевдоним характеристики должен состоять только из английских букв без пробелов.\n" +
                    "Длина псевдонима должна составлять от 3 до 18 символов.\n" +
                    "Пример: название характеристики - \"Диагональ экрана\", псевдоним - \"screenDiagonal\"")
    private String requestParamName;

    private String unit;
}
