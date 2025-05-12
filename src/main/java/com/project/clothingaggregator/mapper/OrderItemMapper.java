package com.project.clothingaggregator.mapper;

import com.project.clothingaggregator.dto.OrderItemResponseDto;
import com.project.clothingaggregator.entity.EbayClothingItem;
import com.project.clothingaggregator.entity.Order;
import com.project.clothingaggregator.entity.OrderItem;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderItemMapper {

    public final ItemMapper itemMapper;

    public OrderItem toEntity(EbayClothingItem item, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrder(order);
        return orderItem;
    }

    public OrderItemResponseDto toResponse(OrderItem item) {
        OrderItemResponseDto dto = new OrderItemResponseDto();
        dto.setOrderId(item.getOrder().getId());
        dto.setItemDto(itemMapper.toResponse(item.getItem()));
        return dto;
    }
}
