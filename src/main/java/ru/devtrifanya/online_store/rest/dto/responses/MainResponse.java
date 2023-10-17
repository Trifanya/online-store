package ru.devtrifanya.online_store.rest.dto.responses;

import lombok.Data;

import ru.devtrifanya.online_store.rest.dto.entities_dto.CategoryDTO;

import java.util.List;

@Data
public class MainResponse {

    private int cartSize;

    private List<CategoryDTO> topCategories;

}
