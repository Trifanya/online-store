package ru.devtrifanya.online_store.services;

<<<<<<< HEAD
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.devtrifanya.online_store.repositories.CartElementRepository;
import ru.devtrifanya.online_store.repositories.ItemRepository;
import ru.devtrifanya.online_store.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
@Data
public class CartElementServiceTest {

    @Mock
    private CartElementRepository cartElementRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private CartElementService cartElementService;

    @Test
    public void getAll_userHasItemsInTheCart_returnCartElementsByUserId() {
        // given

        // when

        // then
    }

    @Test
    public void getAll_userHasNoItemsInTheCart_shouldThrowNotFoundException() {
        // given

        // when

        // then
    }

    @Test
    public void create_shouldSaveCartElement() {
        // given

        // when

        // then
    }

    @Test
    public void update_shouldUpdateCartElementById() {
        // given

        // when

        // then
    }

    @Test
    public void delete_shouldDeleteCartElementById() {
        // given

        // when

        // then
    }
=======
public class CartElementServiceTest {
>>>>>>> LiquibaseInjectionBranch
}
