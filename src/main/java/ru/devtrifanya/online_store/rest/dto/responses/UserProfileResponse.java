package ru.devtrifanya.online_store.rest.dto.responses;

import lombok.Data;
import ru.devtrifanya.online_store.rest.dto.entities_dto.CategoryDTO;
import ru.devtrifanya.online_store.rest.dto.entities_dto.UserDTO;

import java.util.List;

@Data
public class UserProfileResponse {
    private int cartSize;

    private UserDTO user;

    private List<CategoryDTO> rootCategories;
}
