package com.project.clothingaggregator.controller;

import com.project.clothingaggregator.dto.OrderItemRequest;
import com.project.clothingaggregator.dto.OrderItemResponseDto;
import com.project.clothingaggregator.service.OrderItemService;
import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order-items")
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemService orderItemService;

    @GetMapping("/{id}")
    public ResponseEntity<OrderItemResponseDto> getOrderItemById(@PathVariable Integer id) {
        return ResponseEntity.ok(orderItemService.getOrderItemById(id));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItemResponseDto>> getItemsByOrderId(
            @PathVariable Integer orderId) {
        return ResponseEntity.ok(orderItemService.getItemsByOrderId(orderId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItemResponseDto> updateOrderItem(
            @PathVariable Integer id,
            @Valid @RequestBody OrderItemRequest orderItemRequest
    ) {
        return ResponseEntity.ok(orderItemService.updateOrderItem(id, orderItemRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Integer id) {
        orderItemService.deleteOrderItem(id);
        return ResponseEntity.noContent().build();
    }
}
