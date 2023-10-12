package ru.devtrifanya.online_store.rest.dto.entities_dto;

import lombok.Data;

import java.util.List;


@Data
public class CategoryDTO {
    private int id;

    private String name;

    private List<CategoryRelationDTO> relationsWithChildren;
}
