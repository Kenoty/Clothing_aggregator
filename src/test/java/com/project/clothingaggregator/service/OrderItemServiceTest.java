package com.project.clothingaggregator.service;

import com.project.clothingaggregator.dto.*;
import com.project.clothingaggregator.entity.*;
import com.project.clothingaggregator.exception.NotFoundException;
import com.project.clothingaggregator.mapper.*;
import com.project.clothingaggregator.repository.*;
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
class OrderItemServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private OrderItemMapper orderItemMapper;

    @Mock
    private EbayItemMapper ebayItemMapper;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderItemService orderItemService;

    private final Integer TEST_ORDER_ID = 1;
    private final Integer TEST_ITEM_ID = 1;
    private final String TEST_EB_ITEM_ID = "ebay-123";

    @Test
    void getOrderItemById_ExistingId_ReturnsResponse() {
        OrderItem orderItem = new OrderItem();
        OrderItemResponseDto expectedResponse = new OrderItemResponseDto();

        when(orderItemRepository.findById(TEST_ITEM_ID)).thenReturn(Optional.of(orderItem));
        when(orderItemMapper.toResponse(orderItem)).thenReturn(expectedResponse);

        OrderItemResponseDto result = orderItemService.getOrderItemById(TEST_ITEM_ID);

        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(orderItemRepository).findById(TEST_ITEM_ID);
    }

    @Test
    void getOrderItemById_NonExistingId_ThrowsNotFoundException() {
        when(orderItemRepository.findById(TEST_ITEM_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            orderItemService.getOrderItemById(TEST_ITEM_ID);
        });
    }

    @Test
    void getItemsByOrderId_ValidOrder_ReturnsItemList() {
        OrderItem item1 = new OrderItem();
        OrderItem item2 = new OrderItem();
        OrderItemResponseDto dto1 = new OrderItemResponseDto();
        OrderItemResponseDto dto2 = new OrderItemResponseDto();

        when(orderItemRepository.findByOrderId(TEST_ORDER_ID)).thenReturn(List.of(item1, item2));
        when(orderItemMapper.toResponse(item1)).thenReturn(dto1);
        when(orderItemMapper.toResponse(item2)).thenReturn(dto2);

        List<OrderItemResponseDto> result = orderItemService.getItemsByOrderId(TEST_ORDER_ID);

        assertEquals(2, result.size());
        verify(orderItemRepository).findByOrderId(TEST_ORDER_ID);
    }

    @Test
    void updateOrderItem_ValidData_ReturnsUpdatedItem() {
        OrderItemRequest request = new OrderItemRequest();
        request.setItemId(TEST_EB_ITEM_ID);

        OrderItem existingItem = new OrderItem();
        EbayClothingItem newItem = new EbayClothingItem();
        OrderItem savedItem = new OrderItem();
        OrderItemResponseDto expectedResponse = new OrderItemResponseDto();

        when(orderItemRepository.findById(TEST_ITEM_ID)).thenReturn(Optional.of(existingItem));
        when(itemRepository.findById(TEST_EB_ITEM_ID)).thenReturn(Optional.of(newItem));
        when(orderItemRepository.save(existingItem)).thenReturn(savedItem);
        when(orderItemMapper.toResponse(savedItem)).thenReturn(expectedResponse);

        OrderItemResponseDto result = orderItemService.updateOrderItem(TEST_ITEM_ID, request);

        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(orderItemRepository).save(existingItem);
    }

    @Test
    void deleteOrderItem_ExistingId_DeletesItem() {
        when(orderItemRepository.existsById(TEST_ITEM_ID)).thenReturn(true);

        orderItemService.deleteOrderItem(TEST_ITEM_ID);

        verify(orderItemRepository).deleteById(TEST_ITEM_ID);
    }

    @Test
    void deleteOrderItem_NonExistingId_ThrowsException() {
        when(orderItemRepository.existsById(TEST_ITEM_ID)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> {
            orderItemService.deleteOrderItem(TEST_ITEM_ID);
        });
        verify(orderItemRepository, never()).deleteById(any());
    }

    @Test
    void addItemToOrder_ValidData_ReturnsResponse() {
        OrderItemRequest request = new OrderItemRequest();
        request.setItemId(TEST_EB_ITEM_ID);

        Order order = new Order();
        EbayClothingItem item = new EbayClothingItem();
        OrderItem newItem = new OrderItem();
        OrderItem savedItem = new OrderItem();
        OrderItemResponseDto expectedResponse = new OrderItemResponseDto();

        when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.of(order));
        when(itemRepository.findById(TEST_EB_ITEM_ID)).thenReturn(Optional.of(item));
        when(orderItemMapper.toEntity(item, order)).thenReturn(newItem);
        when(orderItemRepository.save(newItem)).thenReturn(savedItem);
        when(orderItemMapper.toResponse(savedItem)).thenReturn(expectedResponse);

        OrderItemResponseDto result = orderItemService.addItemToOrder(TEST_ORDER_ID, request);

        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(orderItemRepository).save(newItem);
    }

    @Test
    void addItemsToOrder_ValidData_ReturnsOrderWithItems() {
        // Arrange
        List<String> itemIds = List.of("item1", "item2");
        Order order = new Order();
        order.setId(TEST_ORDER_ID);

        EbayClothingItem item1 = new EbayClothingItem();
        EbayClothingItem item2 = new EbayClothingItem();

        OrderItem orderItem1 = new OrderItem();
        OrderItem orderItem2 = new OrderItem();

        EbayItemDto dto1 = new EbayItemDto();
        EbayItemDto dto2 = new EbayItemDto();

        OrderWithItemsDto expectedResponse = new OrderWithItemsDto();

        when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.of(order));
        when(itemRepository.findById("item1")).thenReturn(Optional.of(item1));
        when(itemRepository.findById("item2")).thenReturn(Optional.of(item2));
        when(orderItemMapper.toEntity(item1, order)).thenReturn(orderItem1);
        when(orderItemMapper.toEntity(item2, order)).thenReturn(orderItem2);
        when(orderItemRepository.findAllByOrderId(TEST_ORDER_ID)).thenReturn(List.of(item1, item2));
        when(ebayItemMapper.toDto(item1)).thenReturn(dto1);
        when(ebayItemMapper.toDto(item2)).thenReturn(dto2);
        when(orderMapper.toOrderWithItems(order, List.of(dto1, dto2))).thenReturn(expectedResponse);

        OrderWithItemsDto result = orderItemService.addItemsToOrder(TEST_ORDER_ID, itemIds);

        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(orderItemRepository, times(2)).save(any());
    }

    @Test
    void addItemsToOrder_InvalidItemId_ThrowsNotFoundException() {
        List<String> itemIds = List.of("invalid-item");
        Order order = new Order();

        when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.of(order));
        when(itemRepository.findById("invalid-item")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            orderItemService.addItemsToOrder(TEST_ORDER_ID, itemIds);
        });
    }
}