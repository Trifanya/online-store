package ru.devtrifanya.online_store.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReviewDTO {
    @Min(value = 1, message = "Минимальная оценка товара - 1.")
    @Max(value = 5, message = "Максимальная оценка товара - 5.")
    private int stars;

    @Size(min = 10, max = 1000, message = "Максимальная длина комментария - 1000 символов.")
    private String comment;

    //@URL(message = "Укажите url изображения.")
    //private String imageURL;

}
