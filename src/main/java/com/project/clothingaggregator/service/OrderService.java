package com.project.clothingaggregator.service;

import com.project.clothingaggregator.dto.OrderRequest;
import com.project.clothingaggregator.dto.OrderResponseDto;
import com.project.clothingaggregator.entity.Order;
import com.project.clothingaggregator.exception.NotFoundException;
import com.project.clothingaggregator.mapper.OrderMapper;
import com.project.clothingaggregator.repository.OrderRepository;
import com.project.clothingaggregator.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    public OrderResponseDto createOrder(OrderRequest request) {
        if (!userRepository.existsById(request.getUserId())) {
            throw new NotFoundException("User not found. Check user id");
        }

        return orderMapper.toResponse(orderRepository
                 .save(orderMapper.toEntity(
                         userRepository.findById(request.getUserId()), request)));
    }

    public List<OrderResponseDto> getOrders(Integer userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(orderMapper::toResponse).toList();
    }

    public OrderResponseDto getOrderById(Integer orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isEmpty()) {
            throw new NotFoundException();
        }

        Order order = orderOptional.get();
        return orderMapper.toResponse(order);
    }

    public OrderResponseDto  updateOrder(Integer id, OrderRequest orderRequest) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        if (orderRequest.getUserId() != null) {
            order.setUser(userRepository.findById(orderRequest.getUserId())
                    .orElseThrow(() -> new NotFoundException("This user is not exist")));
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
        return orderMapper.toResponse(updatedOrder);
    }

    public void deleteOrder(Integer id) {
        if (!orderRepository.existsById(id)) {
            throw new NotFoundException("Order not found");
        }
        orderRepository.deleteById(id);
    }
}
