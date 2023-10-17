package ru.devtrifanya.online_store.rest.dto.responses;

import lombok.Data;

import ru.devtrifanya.online_store.rest.dto.entities_dto.ItemDTO;
import ru.devtrifanya.online_store.rest.dto.entities_dto.CategoryDTO;
import ru.devtrifanya.online_store.rest.dto.entities_dto.CartElementDTO;

import java.util.List;

@Data
public class CartInfoResponse {

    private List<ItemDTO> items;

    private List<CartElementDTO> userCart;

    private List<CategoryDTO> topCategories;
}
