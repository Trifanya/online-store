package ru.devtrifanya.online_store.rest.dto.entities_dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ItemImageDTO {
    private int id;

    @NotBlank(message = "Необходимо указать url изображения.")
    private String url;
}
