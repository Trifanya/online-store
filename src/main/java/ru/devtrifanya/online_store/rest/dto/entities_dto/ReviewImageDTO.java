package ru.devtrifanya.online_store.rest.dto.entities_dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.devtrifanya.online_store.models.Review;

@Data
public class ReviewImageDTO {
    //private int id;

    @NotBlank(message = "Вы не указали URL изображения.")
    private String url;
}
