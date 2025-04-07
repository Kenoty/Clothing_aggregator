package com.project.clothingaggregator.service;

import com.project.clothingaggregator.dto.OrderItemRequest;
import com.project.clothingaggregator.dto.OrderItemResponseDto;
import com.project.clothingaggregator.dto.OrderRequest;
import com.project.clothingaggregator.dto.OrderResponseDto;
import com.project.clothingaggregator.entity.Order;
import com.project.clothingaggregator.entity.OrderItem;
import com.project.clothingaggregator.entity.Product;
import com.project.clothingaggregator.entity.User;
import com.project.clothingaggregator.exception.NotFoundException;
import com.project.clothingaggregator.mapper.OrderItemMapper;
import com.project.clothingaggregator.mapper.OrderMapper;
import com.project.clothingaggregator.repository.OrderItemRepository;
import com.project.clothingaggregator.repository.OrderRepository;
import com.project.clothingaggregator.repository.ProductRepository;
import com.project.clothingaggregator.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {


    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    public ResponseEntity<OrderResponseDto> createOrder(OrderRequest request) {
        Optional<User> userOptional = userRepository.findById(request.getUserId());
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(OrderMapper.toResponse(orderRepository
                 .save(OrderMapper.toEntity(userOptional, request))));
    }

    public ResponseEntity<List<OrderResponseDto>> getOrders(Integer userId) {
        return ResponseEntity.ok(orderRepository.findByUserId(userId).stream()
                .map(OrderMapper::toResponse).toList());
    }

    public OrderResponseDto getOrderById(Integer orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isEmpty()) {
            throw new NotFoundException();
        }

        Order order = orderOptional.get();
        return OrderMapper.toResponse(order);
    }

    public ResponseEntity<OrderResponseDto>  updateOrder(Integer id, OrderRequest orderRequest) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Order order = orderOptional.get();
        if (orderRequest.getUserId() != null) {
            Optional<User> userOptional = userRepository.findById(orderRequest.getUserId());
            if (userOptional.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            order.setUser(userOptional.get());
        }
        if (orderRequest.getStatus() != null) {
            order.setStatus(orderRequest.getStatus());
        }
        if (orderRequest.getTotalAmount() != null) {
            order.setTotalAmount(orderRequest.getTotalAmount());
        }
        if (orderRequest.getShippingAddress() != null) {
            order.setShippingAddress(orderRequest.getShippingAddress());
        }

        Order updatedOrder = orderRepository.save(order);
        return ResponseEntity.ok(OrderMapper.toResponse(updatedOrder));
    }

    public ResponseEntity<Void> deleteOrder(Integer id) {
        if (!orderRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        orderRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public OrderItemResponseDto addItemToOrder(Integer orderId, OrderItemRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        order.addItem(new OrderItem(product));

        orderRepository.save(order);
        return OrderItemMapper.toResponse(orderItemRepository
                .save(OrderItemMapper.toEntity(product, order)));
    }
}
