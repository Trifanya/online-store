package ru.devtrifanya.online_store.rest.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.devtrifanya.online_store.rest.dto.entities_dto.CategoryDTO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MainResponse {

    private List<CategoryDTO> topCategories;

    private int cartSize;
}
