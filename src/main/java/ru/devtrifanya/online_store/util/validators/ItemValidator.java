package ru.devtrifanya.online_store.util.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.devtrifanya.online_store.dto.ItemDTO;
import ru.devtrifanya.online_store.services.ItemsService;
import ru.devtrifanya.online_store.util.exceptions.item.ItemAlreadyExistException;

@Component
public class ItemValidator implements Validator {
    private final ItemsService itemsService;

    public ItemValidator(ItemsService itemsService) {
        this.itemsService = itemsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return ItemDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ItemDTO itemDTO = (ItemDTO) target;

        if (itemsService.findOne(itemDTO.getName()).isPresent()) {
            throw new ItemAlreadyExistException("Товар с таким названием уже существует.");
        }
    }
}
