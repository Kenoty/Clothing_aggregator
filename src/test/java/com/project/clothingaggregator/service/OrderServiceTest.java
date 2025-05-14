package com.project.clothingaggregator.service;

import com.project.clothingaggregator.dto.OrderRequest;
import com.project.clothingaggregator.dto.OrderResponseDto;
import com.project.clothingaggregator.entity.Order;
import com.project.clothingaggregator.entity.User;
import com.project.clothingaggregator.exception.NotFoundException;
import com.project.clothingaggregator.mapper.OrderMapper;
import com.project.clothingaggregator.repository.OrderRepository;
import com.project.clothingaggregator.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_ValidRequest_ReturnsOrderResponse() {
        // Arrange
        OrderRequest request = new OrderRequest();
        request.setUserId(1);
        request.setStatus("PENDING");
        request.setTotalAmount(BigDecimal.valueOf(100));
        request.setShippingAddress("123 Main St");

        User user = new User();
        user.setId(1);

        Order order = new Order();
        order.setUser(user);

        Order savedOrder = new Order();
        savedOrder.setId(1);
        savedOrder.setUser(user);

        OrderResponseDto expectedResponse = new OrderResponseDto();
        expectedResponse.setId(1);

        when(userRepository.existsById(1)).thenReturn(true);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(orderMapper.toEntity(Optional.of(user), request)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(savedOrder);
        when(orderMapper.toResponse(savedOrder)).thenReturn(expectedResponse);

        // Act
        OrderResponseDto result = orderService.createOrder(request);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(userRepository).existsById(1);
        verify(orderRepository).save(order);
    }

    @Test
    void createOrder_InvalidUserId_ThrowsNotFoundException() {
        OrderRequest request = new OrderRequest();
        request.setUserId(99);

        when(userRepository.existsById(99)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> {
            orderService.createOrder(request);
        });
        verify(userRepository).existsById(99);
        verifyNoInteractions(orderRepository);
    }

    @Test
    void getOrders_ValidUserId_ReturnsOrderList() {
        Integer userId = 1;
        Order order1 = new Order();
        order1.setId(1);
        Order order2 = new Order();
        order2.setId(2);

        OrderResponseDto dto1 = new OrderResponseDto();
        dto1.setId(1);
        OrderResponseDto dto2 = new OrderResponseDto();
        dto2.setId(2);

        when(orderRepository.findByUserId(userId)).thenReturn(List.of(order1, order2));
        when(orderMapper.toResponse(order1)).thenReturn(dto1);
        when(orderMapper.toResponse(order2)).thenReturn(dto2);

        List<OrderResponseDto> result = orderService.getOrders(userId);

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(1).getId());
        verify(orderRepository).findByUserId(userId);
    }

    @Test
    void getOrderById_ExistingOrder_ReturnsOrderResponse() {
        Integer orderId = 1;
        Order order = new Order();
        order.setId(orderId);

        OrderResponseDto expectedResponse = new OrderResponseDto();
        expectedResponse.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderMapper.toResponse(order)).thenReturn(expectedResponse);

        OrderResponseDto result = orderService.getOrderById(orderId);

        assertNotNull(result);
        assertEquals(orderId, result.getId());
        verify(orderRepository).findById(orderId);
    }

    @Test
    void getOrderById_NonExistingOrder_ThrowsNotFoundException() {
        Integer orderId = 99;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            orderService.getOrderById(orderId);
        });
        verify(orderRepository).findById(orderId);
    }

    @Test
    void updateOrder_ValidUpdate_ReturnsUpdatedOrder() {
        Integer orderId = 1;
        OrderRequest updateRequest = new OrderRequest();
        updateRequest.setStatus("SHIPPED");
        updateRequest.setTotalAmount(BigDecimal.valueOf(150));
        updateRequest.setShippingAddress("456 Oak St");

        Order existingOrder = new Order();
        existingOrder.setId(orderId);
        existingOrder.setStatus("PENDING");
        existingOrder.setTotalAmount(BigDecimal.valueOf(100));
        existingOrder.setShippingAddress("123 Main St");

        Order savedOrder = new Order();
        savedOrder.setId(orderId);
        savedOrder.setStatus("SHIPPED");
        savedOrder.setTotalAmount(BigDecimal.valueOf(150));
        savedOrder.setShippingAddress("456 Oak St");

        OrderResponseDto expectedResponse = new OrderResponseDto();
        expectedResponse.setId(orderId);
        expectedResponse.setStatus("SHIPPED");

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(existingOrder)).thenReturn(savedOrder);
        when(orderMapper.toResponse(savedOrder)).thenReturn(expectedResponse);

        OrderResponseDto result = orderService.updateOrder(orderId, updateRequest);

        assertNotNull(result);
        assertEquals("SHIPPED", result.getStatus());
        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(existingOrder);
    }

    @Test
    void updateOrder_ChangeUser_UpdatesUser() {
        Integer orderId = 1;
        Integer newUserId = 2;
        OrderRequest updateRequest = new OrderRequest();
        updateRequest.setUserId(newUserId);

        Order existingOrder = new Order();
        existingOrder.setId(orderId);

        User newUser = new User();
        newUser.setId(newUserId);

        Order savedOrder = new Order();
        savedOrder.setId(orderId);
        savedOrder.setUser(newUser);

        OrderResponseDto expectedResponse = new OrderResponseDto();
        expectedResponse.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(userRepository.findById(newUserId)).thenReturn(Optional.of(newUser));
        when(orderRepository.save(existingOrder)).thenReturn(savedOrder);
        when(orderMapper.toResponse(savedOrder)).thenReturn(expectedResponse);

        OrderResponseDto result = orderService.updateOrder(orderId, updateRequest);

        assertNotNull(result);
        verify(userRepository).findById(newUserId);
        verify(orderRepository).save(existingOrder);
    }

    @Test
    void deleteOrder_ExistingOrder_DeletesOrder() {
        Integer orderId = 1;
        when(orderRepository.existsById(orderId)).thenReturn(true);

        orderService.deleteOrder(orderId);

        verify(orderRepository).existsById(orderId);
        verify(orderRepository).deleteById(orderId);
    }

    @Test
    void deleteOrder_NonExistingOrder_ThrowsNotFoundException() {
        Integer orderId = 99;
        when(orderRepository.existsById(orderId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> {
            orderService.deleteOrder(orderId);
        });
        verify(orderRepository).existsById(orderId);
        verify(orderRepository, never()).deleteById(orderId);
    }
}