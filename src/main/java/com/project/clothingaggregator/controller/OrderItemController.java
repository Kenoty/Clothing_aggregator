package com.project.clothingaggregator.controller;

import com.project.clothingaggregator.dto.OrderItemRequest;
import com.project.clothingaggregator.dto.OrderItemResponseDto;
import com.project.clothingaggregator.entity.OrderItem;
import com.project.clothingaggregator.entity.Product;
import com.project.clothingaggregator.exception.NotFoundException;
import com.project.clothingaggregator.mapper.OrderItemMapper;
import com.project.clothingaggregator.repository.OrderItemRepository;
import com.project.clothingaggregator.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order-items")
public class OrderItemController {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/{id}")
    public ResponseEntity<OrderItemResponseDto> getOrderItemById(@PathVariable Integer id) {
        return ResponseEntity.ok(OrderItemMapper.toResponse(orderItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"))));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItemResponseDto>> getItemsByOrderId(
            @PathVariable Integer orderId) {
        return ResponseEntity.ok(orderItemRepository
                .findByOrderId(orderId).stream().map(OrderItemMapper::toResponse).toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItemResponseDto> updateOrderItem(
            @PathVariable Integer id,
            @RequestBody OrderItemRequest orderItemRequest
    ) {
        Optional<OrderItem> orderItemOptional = orderItemRepository.findById(id);
        if (orderItemOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Product> productOptional = productRepository
                .findById(orderItemRequest.getProductId());
        if (productOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        OrderItem orderItem = orderItemOptional.get();
        orderItem.setProduct(productOptional.get());

        return ResponseEntity.ok(OrderItemMapper.toResponse(orderItemRepository.save(orderItem)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Integer id) {
        if (!orderItemRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        orderItemRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
