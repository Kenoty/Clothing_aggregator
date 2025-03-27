package com.project.clothingaggregator.service;

import com.project.clothingaggregator.dto.OrderRequest;
import com.project.clothingaggregator.entity.Order;
import com.project.clothingaggregator.entity.User;
import com.project.clothingaggregator.repository.OrderRepository;
import com.project.clothingaggregator.repository.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    public ResponseEntity<Order>  UpdateOrder(OrderRequest orderRequest, Integer id) {
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
        return ResponseEntity.ok(updatedOrder);
    }
}
