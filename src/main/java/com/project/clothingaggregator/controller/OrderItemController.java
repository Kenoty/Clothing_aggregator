package com.project.clothingaggregator.controller;

import com.project.clothingaggregator.dto.OrderItemRequest;
import com.project.clothingaggregator.entity.OrderItem;
import com.project.clothingaggregator.entity.Product;
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

    @PostMapping
    public ResponseEntity<OrderItem> createOrderItem(
            @RequestBody OrderItemRequest orderItemRequest) {
        Optional<Product> productOptional = productRepository
                .findById(orderItemRequest.getProductId());
        if (productOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(orderItemRepository
                .save(OrderItemMapper.toEntity(productOptional)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItem> getOrderItemById(@PathVariable Integer id) {
        Optional<OrderItem> orderItemOptional = orderItemRepository.findById(id);
        return orderItemOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItem>> getItemsByOrderId(@PathVariable Integer orderId) {
        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
        return ResponseEntity.ok(items);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItem> updateOrderItem(
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

        OrderItem updatedOrderItem = orderItemRepository.save(orderItem);
        return ResponseEntity.ok(updatedOrderItem);
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
