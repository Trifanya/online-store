package ru.devtrifanya.online_store.content.dto;

import lombok.Data;
import ru.devtrifanya.online_store.models.CategoryRelation;

import java.util.List;


@Data
public class CategoryDTO {
    private String name;
    private List<CategoryRelationDTO> relationsWithChildren;
}
