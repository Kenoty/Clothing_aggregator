package com.project.clothingaggregator.controller;

import com.project.clothingaggregator.dto.OrderItemRequest;
import com.project.clothingaggregator.dto.OrderItemResponseDto;
import com.project.clothingaggregator.dto.OrderRequest;
import com.project.clothingaggregator.dto.OrderResponseDto;
import com.project.clothingaggregator.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(
            @Valid @RequestBody OrderRequest orderRequest) {
        return ResponseEntity.ok(orderService.createOrder(orderRequest));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(orderService.getOrders(userId));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Integer orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDto> updateOrder(
            @PathVariable Integer id,
            @Valid @RequestBody OrderRequest orderRequest) {
        return ResponseEntity.ok(orderService.updateOrder(id, orderRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{orderId}/items")
    public ResponseEntity<OrderItemResponseDto> addItemToOrder(
            @PathVariable Integer orderId,
            @Valid @RequestBody OrderItemRequest request) {
        return ResponseEntity.ok(orderService.addItemToOrder(orderId, request));
    }
}