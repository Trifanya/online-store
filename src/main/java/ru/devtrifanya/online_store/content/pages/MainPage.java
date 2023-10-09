package ru.devtrifanya.online_store.content.pages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.devtrifanya.online_store.content.dto.CategoryDTO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MainPage {

    private List<CategoryDTO> topCategories;

    private int cartSize;
}
