package ru.devtrifanya.online_store.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.User;

import java.time.LocalDateTime;

@Data
public class ReviewDTO {
    @Min(value = 1)
    @Max(value = 5)
    private int stars;

    @Size(min = 10, max = 1000, message = "Максимальная длина комментария - 1000 символов.")
    private String comment;

    //@URL(message = "Укажите url изображения.")
    private String imageURL;

}
