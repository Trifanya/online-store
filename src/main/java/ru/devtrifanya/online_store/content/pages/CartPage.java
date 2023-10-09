package ru.devtrifanya.online_store.content.pages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.devtrifanya.online_store.content.dto.CartElementDTO;
import ru.devtrifanya.online_store.content.dto.CategoryDTO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartPage {

    private List<CartElementDTO> userCart;

    private List<CategoryDTO> topCategories;
}
