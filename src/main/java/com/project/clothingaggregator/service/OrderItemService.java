package com.project.clothingaggregator.service;

import com.project.clothingaggregator.dto.OrderItemRequest;
import com.project.clothingaggregator.dto.OrderItemResponseDto;
import com.project.clothingaggregator.entity.OrderItem;
import com.project.clothingaggregator.exception.NotFoundException;
import com.project.clothingaggregator.mapper.OrderItemMapper;
import com.project.clothingaggregator.repository.OrderItemRepository;
import com.project.clothingaggregator.repository.ItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ItemRepository itemRepository;

    public OrderItemResponseDto getOrderItemById(Integer id) {
        return OrderItemMapper.toResponse(orderItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found")));
    }

    public List<OrderItemResponseDto> getItemsByOrderId(Integer orderId) {
        return orderItemRepository.findByOrderId(orderId).stream()
                .map(OrderItemMapper::toResponse).toList();
    }

    public OrderItemResponseDto updateOrderItem(Integer id, OrderItemRequest request) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        // Product product = productRepository.findById(request.getProductId())
            //    .orElseThrow(NotFoundException::new);

       // orderItem.setProduct(product);

        return OrderItemMapper.toResponse(orderItemRepository.save(orderItem));
    }

    public void deleteOrderItem(Integer id) {
        if (!orderItemRepository.existsById(id)) {
            throw new NotFoundException("OrderItem not found with id: " + id);
        }
        orderItemRepository.deleteById(id);
    }
}
