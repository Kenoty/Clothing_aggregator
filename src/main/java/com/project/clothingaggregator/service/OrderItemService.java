package com.project.clothingaggregator.service;

import com.project.clothingaggregator.dto.EbayItemDto;
import com.project.clothingaggregator.dto.OrderItemRequest;
import com.project.clothingaggregator.dto.OrderItemResponseDto;
import com.project.clothingaggregator.dto.OrderWithItemsDto;
import com.project.clothingaggregator.entity.EbayClothingItem;
import com.project.clothingaggregator.entity.Order;
import com.project.clothingaggregator.entity.OrderItem;
import com.project.clothingaggregator.exception.NotFoundException;
import com.project.clothingaggregator.mapper.EbayItemMapper;
import com.project.clothingaggregator.mapper.OrderItemMapper;
import com.project.clothingaggregator.mapper.OrderMapper;
import com.project.clothingaggregator.repository.ItemRepository;
import com.project.clothingaggregator.repository.OrderItemRepository;
import com.project.clothingaggregator.repository.OrderRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ItemRepository itemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final EbayItemMapper ebayItemMapper;

    public OrderItemResponseDto getOrderItemById(Integer id) {
        return orderItemMapper.toResponse(orderItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found")));
    }

    public List<OrderItemResponseDto> getItemsByOrderId(Integer orderId) {
        return orderItemRepository.findByOrderId(orderId).stream()
                .map(orderItemMapper::toResponse).toList();
    }

    public OrderItemResponseDto updateOrderItem(Integer id, OrderItemRequest request) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        EbayClothingItem item = itemRepository.findById(request.getItemId())
                .orElseThrow(NotFoundException::new);

        orderItem.setItem(item);
        return orderItemMapper.toResponse(orderItemRepository.save(orderItem));
    }

    public void deleteOrderItem(Integer id) {
        if (!orderItemRepository.existsById(id)) {
            throw new NotFoundException("OrderItem not found with id: " + id);
        }
        orderItemRepository.deleteById(id);
    }

    public OrderItemResponseDto addItemToOrder(Integer orderId, OrderItemRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        EbayClothingItem item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new NotFoundException("Item not found"));

        return orderItemMapper.toResponse(orderItemRepository
                .save(orderItemMapper.toEntity(item, order)));
    }

    public OrderWithItemsDto addItemsToOrder(Integer orderId, List<String> ids) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        ids.forEach(id -> {
            EbayClothingItem item = itemRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Item not found"));
            orderItemRepository.save(orderItemMapper.toEntity(item, order));
        });

        List<EbayItemDto> items = new ArrayList<>(orderItemRepository.findAllByOrderId(orderId))
                .stream().map(ebayItemMapper::toDto).toList();

        return orderMapper.toOrderWithItems(order, items);
    }
}
