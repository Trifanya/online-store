package ru.devtrifanya.online_store.rest.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.devtrifanya.online_store.rest.dto.entities_dto.CartElementDTO;
import ru.devtrifanya.online_store.rest.dto.entities_dto.CategoryDTO;
import ru.devtrifanya.online_store.rest.dto.entities_dto.ItemDTO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartInfoResponse {

    private List<CartElementDTO> userCart;

    private List<ItemDTO> items;

    private List<CategoryDTO> topCategories;
}
