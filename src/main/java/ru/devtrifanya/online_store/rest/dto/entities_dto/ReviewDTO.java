package ru.devtrifanya.online_store.rest.dto.entities_dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class ReviewDTO {
    private int id;

    @Min(value = 1, message = "Минимальная оценка товара - 1.")
    @Max(value = 5, message = "Максимальная оценка товара - 5.")
    private int stars;

    @Size(min = 10, max = 1000, message = "Максимальная длина комментария - 1000 символов.")
    private String comment;

    private List<String> images;

}
