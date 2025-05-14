package com.project.clothingaggregator.service;

import com.project.clothingaggregator.entity.EbayClothingItem;
import com.project.clothingaggregator.entity.User;
import com.project.clothingaggregator.entity.UserFavorite;
import com.project.clothingaggregator.entity.UserFavoriteId;
import com.project.clothingaggregator.exception.AlreadyExistsException;
import com.project.clothingaggregator.exception.NotFoundException;
import com.project.clothingaggregator.repository.ItemRepository;
import com.project.clothingaggregator.repository.UserFavoriteRepository;
import com.project.clothingaggregator.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserFavoriteServiceTest {

    @Mock
    private UserFavoriteRepository userFavoriteRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private UserFavoriteService userFavoriteService;

    private final Integer TEST_USER_ID = 1;
    private final String TEST_ITEM_ID = "item-123";

    @Test
    void addToFavorites_ValidData_ReturnsFavorite() {
        User user = new User();
        user.setId(TEST_USER_ID);

        EbayClothingItem item = new EbayClothingItem();
        item.setItemId(TEST_ITEM_ID);

        UserFavoriteId favoriteId = new UserFavoriteId(TEST_USER_ID, TEST_ITEM_ID);
        UserFavorite expectedFavorite = new UserFavorite();
        expectedFavorite.setId(favoriteId);
        expectedFavorite.setUser(user);
        expectedFavorite.setItem(item);

        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(user));
        when(itemRepository.findById(TEST_ITEM_ID)).thenReturn(Optional.of(item));
        when(userFavoriteRepository.existsById(favoriteId)).thenReturn(false);
        when(userFavoriteRepository.save(any(UserFavorite.class))).thenReturn(expectedFavorite);

        UserFavorite result = userFavoriteService.addToFavorites(TEST_USER_ID, TEST_ITEM_ID);

        assertNotNull(result);
        assertEquals(TEST_USER_ID, result.getId().getUserId());
        assertEquals(TEST_ITEM_ID, result.getId().getItemId());
        verify(userFavoriteRepository).save(any(UserFavorite.class));
    }

    @Test
    void addToFavorites_UserNotFound_ThrowsNotFoundException() {
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            userFavoriteService.addToFavorites(TEST_USER_ID, TEST_ITEM_ID);
        });
        verify(userRepository).findById(TEST_USER_ID);
        verifyNoInteractions(itemRepository, userFavoriteRepository);
    }

    @Test
    void addToFavorites_ItemNotFound_ThrowsNotFoundException() {
        User user = new User();
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(user));
        when(itemRepository.findById(TEST_ITEM_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            userFavoriteService.addToFavorites(TEST_USER_ID, TEST_ITEM_ID);
        });
        verify(itemRepository).findById(TEST_ITEM_ID);
        verify(userFavoriteRepository, never()).save(any());
    }

    @Test
    void addToFavorites_AlreadyExists_ThrowsAlreadyExistsException() {
        User user = new User();
        EbayClothingItem item = new EbayClothingItem();
        UserFavoriteId favoriteId = new UserFavoriteId(TEST_USER_ID, TEST_ITEM_ID);

        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(user));
        when(itemRepository.findById(TEST_ITEM_ID)).thenReturn(Optional.of(item));
        when(userFavoriteRepository.existsById(favoriteId)).thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> {
            userFavoriteService.addToFavorites(TEST_USER_ID, TEST_ITEM_ID);
        });
        verify(userFavoriteRepository, never()).save(any());
    }

    @Test
    void getUserFavorites_ValidUserId_ReturnsFavoritesList() {
        UserFavorite favorite1 = new UserFavorite();
        UserFavorite favorite2 = new UserFavorite();

        when(userFavoriteRepository.findAllByUserId(TEST_USER_ID)).thenReturn(List.of(favorite1, favorite2));

        List<UserFavorite> result = userFavoriteService.getUserFavorites(TEST_USER_ID);

        assertEquals(2, result.size());
        verify(userFavoriteRepository).findAllByUserId(TEST_USER_ID);
    }

    @Test
    void isFavorite_Exists_ReturnsTrue() {
        UserFavoriteId favoriteId = new UserFavoriteId(TEST_USER_ID, TEST_ITEM_ID);
        when(userFavoriteRepository.existsById(favoriteId)).thenReturn(true);

        boolean result = userFavoriteService.isFavorite(TEST_USER_ID, TEST_ITEM_ID);

        assertTrue(result);
        verify(userFavoriteRepository).existsById(favoriteId);
    }

    @Test
    void isFavorite_NotExists_ReturnsFalse() {
        UserFavoriteId favoriteId = new UserFavoriteId(TEST_USER_ID, TEST_ITEM_ID);
        when(userFavoriteRepository.existsById(favoriteId)).thenReturn(false);

        boolean result = userFavoriteService.isFavorite(TEST_USER_ID, TEST_ITEM_ID);

        assertFalse(result);
        verify(userFavoriteRepository).existsById(favoriteId);
    }

    @Test
    void removeFromFavorites_ValidData_DeletesFavorite() {
        UserFavoriteId favoriteId = new UserFavoriteId(TEST_USER_ID, TEST_ITEM_ID);
        doNothing().when(userFavoriteRepository).deleteById(favoriteId);

        userFavoriteService.removeFromFavorites(TEST_USER_ID, TEST_ITEM_ID);

        verify(userFavoriteRepository).deleteById(favoriteId);
    }

    @Test
    void countUserFavorites_ValidUserId_ReturnsCount() {
        long expectedCount = 5L;
        when(userFavoriteRepository.countByUserId(TEST_USER_ID)).thenReturn(expectedCount);

        long result = userFavoriteService.countUserFavorites(TEST_USER_ID);

        assertEquals(expectedCount, result);
        verify(userFavoriteRepository).countByUserId(TEST_USER_ID);
    }
}