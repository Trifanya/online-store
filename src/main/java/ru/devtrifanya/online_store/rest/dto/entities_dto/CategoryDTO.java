package ru.devtrifanya.online_store.rest.dto.entities_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;


@Data
public class CategoryDTO {
    private int id;

    @NotBlank(message = "Название категории не должно быть пустым.")
    private String name;

    private List<CategoryRelationDTO> relationsWithChildren;
}
