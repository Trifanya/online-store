package ru.devtrifanya.online_store.rest.dto.entities_dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CategoryRelationDTO {
    private int id;

    @NotNull(message = "Для перемещения категории необходимо указать новую родительскую категорию.")
    private @Valid CategoryDTO child;
}
