package ru.devtrifanya.online_store.util.validators;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.devtrifanya.online_store.dto.ItemDTO;
import ru.devtrifanya.online_store.repositories.ItemRepository;
import ru.devtrifanya.online_store.services.ItemService;
import ru.devtrifanya.online_store.util.exceptions.item.ItemAlreadyExistException;

@Component
@Data
public class ItemValidator implements Validator {
    private final ItemRepository itemRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return ItemDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ItemDTO itemDTO = (ItemDTO) target;

        if (itemRepository.findByName(itemDTO.getName()).isPresent()) {
            throw new ItemAlreadyExistException();
        }
    }
}
