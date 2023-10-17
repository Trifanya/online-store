package ru.devtrifanya.online_store.rest.dto.entities_dto;

import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

@Data
public class ReviewDTO {
    private int id;

    @Min(value = 1, message = "Минимальная оценка товара - 1.")
    @Max(value = 5, message = "Максимальная оценка товара - 5.")
    private int stars;

    @Size(min = 10, max = 1000, message = "Длина комментария должна составлять не менее 10 и не более 1000 символов.")
    private String comment;

    private List<@Valid ReviewImageDTO> images;

}
