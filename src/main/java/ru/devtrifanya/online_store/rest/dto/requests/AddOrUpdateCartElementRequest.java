package ru.devtrifanya.online_store.rest.dto.requests;

import jakarta.validation.Valid;
import lombok.Data;
import ru.devtrifanya.online_store.rest.dto.entities_dto.CartElementDTO;

@Data
public class AddOrUpdateCartElementRequest {
    private @Valid CartElementDTO cartElement;
}
