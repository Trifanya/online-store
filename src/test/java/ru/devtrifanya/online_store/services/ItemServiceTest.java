package ru.devtrifanya.online_store.services;

import io.jsonwebtoken.lang.Assert;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.repositories.ItemRepository;
import ru.devtrifanya.online_store.services.ItemService;

@ExtendWith(MockitoExtension.class)
@Data
public class ItemServiceTest {

    @Mock
    private final ItemRepository itemRepository;

    @InjectMocks
    private final ItemService itemService;

    @Test
    public void firstTestMethod() {

    }
}
