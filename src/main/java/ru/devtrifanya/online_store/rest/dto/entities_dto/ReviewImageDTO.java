package ru.devtrifanya.online_store.rest.dto.entities_dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class ReviewImageDTO {
    @NotBlank(message = "Вы не указали URL изображения.")
    private String url;
}
