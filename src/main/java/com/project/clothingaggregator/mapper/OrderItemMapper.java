package com.project.clothingaggregator.mapper;

import com.project.clothingaggregator.dto.OrderItemResponseDto;
import com.project.clothingaggregator.entity.Order;
import com.project.clothingaggregator.entity.OrderItem;
import com.project.clothingaggregator.entity.Product;
import org.modelmapper.ModelMapper;

public class OrderItemMapper {

    public static final ModelMapper modelMapper = new ModelMapper();

    public static OrderItem toEntity(Product product, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setOrder(order);
        return orderItem;
    }

    public static OrderItemResponseDto toResponse(OrderItem item) {
        OrderItemResponseDto dto = new OrderItemResponseDto();
        dto.setOrderId(item.getOrder().getId());
        dto.setProductDto(ProductMapper.toResponse(item.getProduct()));
        return dto;
    }
}
