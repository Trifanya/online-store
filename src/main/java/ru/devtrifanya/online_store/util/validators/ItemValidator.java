package ru.devtrifanya.online_store.util.validators;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.devtrifanya.online_store.dto.ItemDTO;
import ru.devtrifanya.online_store.repositories.ItemRepository;
import ru.devtrifanya.online_store.util.exceptions.AlreadyExistException;

@Component
@Data
public class ItemValidator {
    private final ItemRepository itemRepository;

    public void validate(ItemDTO itemDTO) {
        if (itemRepository.findByName(itemDTO.getName()).isPresent()) {
            throw new AlreadyExistException("Товар с таким названием уже есть на сайте.");
        }
    }
}
