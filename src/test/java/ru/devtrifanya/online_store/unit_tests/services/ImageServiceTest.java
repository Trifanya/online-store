package ru.devtrifanya.online_store.unit_tests.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.devtrifanya.online_store.models.Item;
import ru.devtrifanya.online_store.models.ItemImage;
import ru.devtrifanya.online_store.services.ItemService;
import ru.devtrifanya.online_store.services.ImageService;
import ru.devtrifanya.online_store.repositories.ItemImageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {
    @Mock
    private ItemService itemServiceMock;
    @Mock
    private ItemImageRepository itemImageRepoMock;

    @InjectMocks
    private ImageService testingService;

    private int itemId = 1;
    private int foundImageId = 1;
    private int savedImageId = 2;

    private Item foundItem = new Item(itemId, "found", "m1", 1000, 1, "d1", 3.5);

    private ItemImage imageToSaveWithNewUrl = new ItemImage(0, "url1");
    private ItemImage imageToSaveWithExistedUrl = new ItemImage(0, "url2");
    private ItemImage foundImage = new ItemImage(
            foundImageId, "url2",
            new ArrayList<>(List.of(new Item(), new Item()))
    );

    @Test
    public void createNewImageIfNotExist_imageIsNotExist_shouldAssignId() {
        // Определение поведения mock-объектов
        createNewImageIfNotExist_determineBehaviorOfMocksIfNotExist();

        // Выполнение тестируемого метода
        ItemImage resultImage = testingService.createNewImageIfNotExist(imageToSaveWithNewUrl, itemId);
        // Проверка совпадения ожидаемого результата с реальным
        Mockito.verify(itemImageRepoMock).save(imageToSaveWithNewUrl);
        Assertions.assertNotNull(resultImage.getId());
    }

    @Test
    public void createNewImageIfNotExist_imageIsNotExist_shouldAssignNewItemsList() {
        // Определение поведения mock-объектов
        createNewImageIfNotExist_determineBehaviorOfMocksIfNotExist();

        // Выполнение тестируемого метода
        ItemImage resultImage = testingService.createNewImageIfNotExist(imageToSaveWithNewUrl, itemId);
        // Проверка совпадения ожидаемого результата с реальным
        Mockito.verify(itemServiceMock).getItem(itemId);
        Mockito.verify(itemImageRepoMock).findByUrl(imageToSaveWithNewUrl.getUrl());
        Assertions.assertEquals(List.of(foundItem), resultImage.getItems());
    }

    @Test
    public void createNewImageIfNotExist_imageIsExist_shouldAddItemToList() {
        // Определение поведения mock-объектов
        createNewImageIfNotExist_determineBehaviorOfMocksIfExist();

        // Выполнение тестируемого метода
        ItemImage resultImage = testingService.createNewImageIfNotExist(imageToSaveWithExistedUrl, itemId);
        // Проверка совпадения ожидаемого результата с реальным
        Mockito.verify(itemServiceMock).getItem(itemId);
        Mockito.verify(itemImageRepoMock).findByUrl(imageToSaveWithExistedUrl.getUrl());
        Assertions.assertTrue(resultImage.getItems().contains(foundItem));
    }

    @Test
    public void createNewImageIfNotExist_imageIsExist_shouldSaveUpdatedImage() {
        // Определение поведения mock-объектов
        createNewImageIfNotExist_determineBehaviorOfMocksIfExist();

        // Выполнение тестируемого метода
        ItemImage resultImage = testingService.createNewImageIfNotExist(imageToSaveWithExistedUrl, itemId);
        // Проверка совпадения ожидаемого результата с реальным
        Mockito.verify(itemImageRepoMock).save(foundImage);
    }


    public void createNewImageIfNotExist_determineBehaviorOfMocksIfNotExist() {
        Mockito.when(itemServiceMock.getItem(itemId))
                .thenReturn(foundItem);
        Mockito.when(itemImageRepoMock.findByUrl(imageToSaveWithNewUrl.getUrl()))
                .thenReturn(Optional.empty());
        Mockito.doAnswer(invocationOnMock -> {
            ItemImage image = invocationOnMock.getArgument(0);
            image.setId(savedImageId);
            return image;
        }).when(itemImageRepoMock).save(imageToSaveWithNewUrl);
    }

    public void createNewImageIfNotExist_determineBehaviorOfMocksIfExist() {
        Mockito.when(itemServiceMock.getItem(itemId))
                .thenReturn(foundItem);
        Mockito.when(itemImageRepoMock.findByUrl(imageToSaveWithExistedUrl.getUrl()))
                .thenReturn(Optional.of(foundImage));
        Mockito.doAnswer(invocationOnMock -> invocationOnMock.getArgument(0))
                .when(itemImageRepoMock).save(any(ItemImage.class));
    }
}
