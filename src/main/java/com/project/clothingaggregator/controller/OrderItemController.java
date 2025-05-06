package com.project.clothingaggregator.controller;

import com.project.clothingaggregator.dto.OrderItemRequest;
import com.project.clothingaggregator.dto.OrderItemResponseDto;
import com.project.clothingaggregator.dto.OrderWithItemsDto;
import com.project.clothingaggregator.service.OrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order-items")
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemService orderItemService;

    @PostMapping("/{orderId}/item")
    @Operation(
            summary = "Add item to order",
            description = "Adds a new item to an existing order")
    public ResponseEntity<OrderItemResponseDto> addItemToOrder(
            @PathVariable Integer orderId,
            @Valid @RequestBody OrderItemRequest request) {
        return ResponseEntity.ok(orderItemService.addItemToOrder(orderId, request));
    }

    @PostMapping("/{orderId}/items")
    @Operation(
            summary = "Add items to order",
            description = "Adds multiple items to an existing order by their IDs. ")
    public ResponseEntity<OrderWithItemsDto> addItemsToOrder(
            @PathVariable Integer orderId,
            @Valid @RequestBody List<String> itemIds) {
        return ResponseEntity.ok(orderItemService.addItemsToOrder(orderId, itemIds));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get order item by ID",
            description = "Retrieves a specific order item by its unique identifier")
    public ResponseEntity<OrderItemResponseDto> getOrderItemById(@PathVariable Integer id) {
        return ResponseEntity.ok(orderItemService.getOrderItemById(id));
    }

    @GetMapping("/order/{orderId}")
    @Operation(
            summary = "Get items by order ID",
            description = "Retrieves all items belonging to a specific order")
    public ResponseEntity<List<OrderItemResponseDto>> getItemsByOrderId(
            @PathVariable Integer orderId) {
        return ResponseEntity.ok(orderItemService.getItemsByOrderId(orderId));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update order item",
            description = "Updates an existing order item with new data")
    public ResponseEntity<OrderItemResponseDto> updateOrderItem(
            @PathVariable Integer id,
            @Valid @RequestBody OrderItemRequest orderItemRequest
    ) {
        return ResponseEntity.ok(orderItemService.updateOrderItem(id, orderItemRequest));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete order item",
            description = "Removes an order item from the system")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Integer id) {
        orderItemService.deleteOrderItem(id);
        return ResponseEntity.noContent().build();
    }
}
