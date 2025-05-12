package com.project.clothingaggregator.service;

import com.project.clothingaggregator.dto.EbayItemDto;
import com.project.clothingaggregator.entity.EbayClothingItem;
import com.project.clothingaggregator.exception.NotFoundException;
import com.project.clothingaggregator.mapper.ItemMapper;
import com.project.clothingaggregator.repository.ItemRepository;
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
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private ItemService itemService;

    private final String TEST_ITEM_ID = "item-123";
    private final EbayItemDto TEST_ITEM_DTO = new EbayItemDto();
    private final EbayClothingItem TEST_ENTITY = new EbayClothingItem();

    @Test
    void createItem_ValidRequest_ReturnsItemDto() {
        when(itemMapper.toEntity(TEST_ITEM_DTO)).thenReturn(TEST_ENTITY);
        when(itemRepository.save(TEST_ENTITY)).thenReturn(TEST_ENTITY);
        when(itemMapper.toResponse(TEST_ENTITY)).thenReturn(TEST_ITEM_DTO);

        EbayItemDto result = itemService.createItem(TEST_ITEM_DTO);

        assertNotNull(result);
        assertEquals(TEST_ITEM_DTO, result);
        verify(itemRepository).save(TEST_ENTITY);
    }

    @Test
    void getItem_ExistingId_ReturnsItemDto() {
        when(itemRepository.findById(TEST_ITEM_ID)).thenReturn(Optional.of(TEST_ENTITY));
        when(itemMapper.toResponse(TEST_ENTITY)).thenReturn(TEST_ITEM_DTO);

        EbayItemDto result = itemService.getItem(TEST_ITEM_ID);

        assertNotNull(result);
        assertEquals(TEST_ITEM_DTO, result);
        verify(itemRepository).findById(TEST_ITEM_ID);
    }

    @Test
    void getItem_NonExistingId_ThrowsNotFoundException() {
        when(itemRepository.findById(TEST_ITEM_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            itemService.getItem(TEST_ITEM_ID);
        });
    }

    @Test
    void getAllItems_ReturnsItemList() {
        EbayClothingItem item1 = new EbayClothingItem();
        EbayClothingItem item2 = new EbayClothingItem();
        EbayItemDto dto1 = new EbayItemDto();
        EbayItemDto dto2 = new EbayItemDto();

        when(itemRepository.findAll()).thenReturn(List.of(item1, item2));
        when(itemMapper.toResponse(item1)).thenReturn(dto1);
        when(itemMapper.toResponse(item2)).thenReturn(dto2);

        List<EbayItemDto> result = itemService.getAllItems();

        assertEquals(2, result.size());
        verify(itemRepository).findAll();
    }

    @Test
    void updateItem_ExistingId_ReturnsUpdatedItem() {
        EbayItemDto updatedDto = new EbayItemDto();
        updatedDto.setTitle("Updated Title");

        when(itemRepository.findById(TEST_ITEM_ID)).thenReturn(Optional.of(TEST_ENTITY));
        when(itemMapper.updateFromRequest(updatedDto, TEST_ENTITY)).thenReturn(TEST_ENTITY);
        when(itemRepository.save(TEST_ENTITY)).thenReturn(TEST_ENTITY);
        when(itemMapper.toResponse(TEST_ENTITY)).thenReturn(updatedDto);

        EbayItemDto result = itemService.updateItem(TEST_ITEM_ID, updatedDto);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        verify(itemRepository).save(TEST_ENTITY);
    }

    @Test
    void updateItem_NonExistingId_ThrowsNotFoundException() {
        when(itemRepository.findById(TEST_ITEM_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            itemService.updateItem(TEST_ITEM_ID, new EbayItemDto());
        });
        verify(itemRepository, never()).save(any());
    }

    @Test
    void deleteItem_ExistingId_DeletesItem() {
        when(itemRepository.existsById(TEST_ITEM_ID)).thenReturn(true);

        itemService.deleteItem(TEST_ITEM_ID);

        verify(itemRepository).deleteById(TEST_ITEM_ID);
    }

    @Test
    void deleteItem_NonExistingId_ThrowsNotFoundException() {
        when(itemRepository.existsById(TEST_ITEM_ID)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> {
            itemService.deleteItem(TEST_ITEM_ID);
        });
        verify(itemRepository, never()).deleteById(any());
    }
}