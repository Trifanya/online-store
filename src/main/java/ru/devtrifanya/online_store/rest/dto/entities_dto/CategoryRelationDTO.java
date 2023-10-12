package ru.devtrifanya.online_store.rest.dto.entities_dto;

import lombok.Data;

@Data
public class CategoryRelationDTO {
    private int id;

    private CategoryDTO child;
}
