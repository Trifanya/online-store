package ru.devtrifanya.online_store.rest.dto.entities_dto;

import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Data
public class CategoryRelationDTO {
    private int id;

    @NotNull(message = "Для перемещения категории необходимо указать новую родительскую категорию.")
    private @Valid CategoryDTO child;
}
