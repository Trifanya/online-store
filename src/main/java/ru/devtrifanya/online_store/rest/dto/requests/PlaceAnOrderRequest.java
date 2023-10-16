package ru.devtrifanya.online_store.rest.dto.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import ru.devtrifanya.online_store.rest.dto.entities_dto.CartElementDTO;

import java.util.List;

@Data
public class PlaceAnOrderRequest {
    @NotEmpty(message = "Корзина не должна быть пустая.")
    List<@Valid CartElementDTO> cartContent;
}
