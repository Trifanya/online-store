package ru.devtrifanya.online_store.rest.dto.entities_dto;

import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;


@Data
public class CategoryDTO {
    private int id;

    @NotBlank(message = "Название категории не должно быть пустым.")
    private String name;

    private List<@Valid CategoryDTO> children;
}
