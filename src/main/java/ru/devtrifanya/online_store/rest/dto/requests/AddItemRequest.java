package ru.devtrifanya.online_store.rest.dto.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.devtrifanya.online_store.rest.dto.entities_dto.ItemImageDTO;
import ru.devtrifanya.online_store.rest.dto.entities_dto.ItemDTO;
import ru.devtrifanya.online_store.rest.dto.entities_dto.ItemFeatureDTO;

import java.util.List;
import java.util.Map;

@Data
public class AddItemRequest {
    private @Valid ItemDTO item;

    @NotNull(message = "Необходимо указать категорию товара.")
    private int categoryId;

    private List<@Valid ItemImageDTO> itemImages;

    private Map<Integer, @Valid ItemFeatureDTO> itemFeatures; // Map<(id характеристики категории), (значение характеристики товара)>
}
