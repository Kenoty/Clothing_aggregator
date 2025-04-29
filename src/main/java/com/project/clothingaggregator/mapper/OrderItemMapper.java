package com.project.clothingaggregator.mapper;

import com.project.clothingaggregator.dto.OrderItemResponseDto;
import com.project.clothingaggregator.entity.EbayClothingItem;
import com.project.clothingaggregator.entity.Order;
import com.project.clothingaggregator.entity.OrderItem;
import org.modelmapper.ModelMapper;

public class OrderItemMapper {

    public static final ModelMapper modelMapper = new ModelMapper();

    public static OrderItem toEntity(EbayClothingItem item, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrder(order);
        return orderItem;
    }

    public static OrderItemResponseDto toResponse(OrderItem item) {
        OrderItemResponseDto dto = new OrderItemResponseDto();
        dto.setOrderId(item.getOrder().getId());
        dto.setItemDto(ItemMapper.toResponse(item.getItem()));
        return dto;
    }
}
