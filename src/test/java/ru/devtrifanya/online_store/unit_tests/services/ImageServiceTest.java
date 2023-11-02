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

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

    private static final int ITEM_ID = 1;
    private static final int IMAGE_ID = 1;
    private static final int FOUND_IMAGE_ID = 2;
    private static final String URL = "url";

    @Mock
    private ItemService itemServiceMock;
    @Mock
    private ItemImageRepository itemImageRepoMock;

    @InjectMocks
    private ImageService testingService;

    @Test
    public void createNewImageIfNotExist_imageIsNotExist_shouldAssignIdAndNewItemsList() {
        // Given
        ItemImage imageToSave = getImageWithoutId();
        mockGetItem();
        mockFindByUrl_notExist();
        mockSaveNew();

        // When
        ItemImage resultImage = testingService.createNewImageIfNotExist(imageToSave, ITEM_ID);

        // Then
        Mockito.verify(itemServiceMock).getItem(ITEM_ID);
        Mockito.verify(itemImageRepoMock).findByUrl(URL);
        Mockito.verify(itemImageRepoMock).save(imageToSave);
        Assertions.assertNotNull(resultImage.getId());
        Assertions.assertEquals(List.of(getItem()), resultImage.getItems());
    }

    @Test
    public void createNewImageIfNotExist_imageIsExist_shouldAddItemToList() {
        // Given
        ItemImage imageToSave = getImageWithId(IMAGE_ID);
        ItemImage imageToUpdate = getImageWithId(FOUND_IMAGE_ID);
        imageToUpdate.getItems().add(getItem());
        mockGetItem();
        mockFindByUrl_exist();
        mockSaveUpdated();

        // When
        ItemImage resultImage = testingService.createNewImageIfNotExist(imageToSave, ITEM_ID);

        // Then
        Mockito.verify(itemServiceMock).getItem(ITEM_ID);
        Mockito.verify(itemImageRepoMock).findByUrl(URL);
        Mockito.verify(itemImageRepoMock).save(imageToUpdate);
        Assertions.assertTrue(resultImage.getItems().contains(getItem()));
    }

    // Определение поведения mock-объектов.

    private void mockGetItem() {
        Mockito.when(itemServiceMock.getItem(ITEM_ID))
                .thenReturn(getItem());
    }
    private void mockFindByUrl_exist() {
        Mockito.when(itemImageRepoMock.findByUrl(URL))
                .thenReturn(Optional.of(getImageWithId(FOUND_IMAGE_ID)));
    }
    private void mockFindByUrl_notExist() {
        Mockito.when(itemImageRepoMock.findByUrl(URL))
                .thenReturn(Optional.empty());
    }
    private void mockSaveNew() {
        Mockito.doAnswer(invocationOnMock -> {
            ItemImage image = invocationOnMock.getArgument(0);
            image.setId(IMAGE_ID);
            return image;
        }).when(itemImageRepoMock).save(any(ItemImage.class));
    }
    private void mockSaveUpdated() {
        Mockito.doAnswer(invocationOnMock -> invocationOnMock.getArgument(0))
                .when(itemImageRepoMock).save(any(ItemImage.class));
    }

    // Вспомогательные методы.

    private Item getItem() {
        return new Item()
                .setId(ITEM_ID);
    }

    private ItemImage getImageWithId(int imageId) {
        return new ItemImage()
                .setId(imageId)
                .setUrl(URL)
                .setItems(new ArrayList<>());
    }

    private ItemImage getImageWithoutId() {
        return new ItemImage()
                .setUrl(URL);
    }
}
