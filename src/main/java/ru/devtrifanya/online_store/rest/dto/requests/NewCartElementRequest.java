package ru.devtrifanya.online_store.rest.dto.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.devtrifanya.online_store.rest.dto.entities_dto.CartElementDTO;

@Data
public class NewCartElementRequest {
    private @Valid CartElementDTO cartElement;

    private int itemId;

}
