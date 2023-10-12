package ru.devtrifanya.online_store.rest.dto.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.devtrifanya.online_store.rest.dto.entities_dto.ImageDTO;
import ru.devtrifanya.online_store.rest.dto.entities_dto.ItemDTO;
import ru.devtrifanya.online_store.rest.dto.entities_dto.ItemFeatureDTO;

import java.util.List;

@Data
public class NewItemRequest {
    @NotNull
    private ItemDTO item;

    private int categoryId;

    private List<ImageDTO> itemImages;

    private List<ItemFeatureDTO> itemFeatures;
}
