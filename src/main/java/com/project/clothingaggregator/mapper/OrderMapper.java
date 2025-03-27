package com.project.clothingaggregator.mapper;

import com.project.clothingaggregator.dto.OrderRequest;
import com.project.clothingaggregator.dto.OrderResponseDto;
import com.project.clothingaggregator.entity.Order;
import com.project.clothingaggregator.entity.User;
import com.project.clothingaggregator.exception.NotFoundException;
import java.util.Optional;
import org.modelmapper.ModelMapper;

public class OrderMapper {

    public final static ModelMapper modelMapper = new ModelMapper();

    public static Order toEntity(Optional<User> user, OrderRequest request) {
        Order order = new Order();
        order.setUser(user.orElseThrow(() ->
                new NotFoundException("User not found")));
        order.setStatus(request.getStatus());
        order.setTotalAmount(request.getTotalAmount());
        order.setShippingAddress(request.getShippingAddress());
        return order;
    }

    public static OrderResponseDto toResponse(Order entity) {
        return modelMapper.map(entity, OrderResponseDto.class);
    }
}
