package com.project.clothingaggregator.mapper;

import com.project.clothingaggregator.dto.EbayItemDto;
import com.project.clothingaggregator.dto.OrderWithItemsDto;
import com.project.clothingaggregator.dto.UserDto;
import com.project.clothingaggregator.dto.UserWithOrdersDto;
import com.project.clothingaggregator.entity.OrderItem;
import com.project.clothingaggregator.entity.User;
import org.modelmapper.ModelMapper;

public class UserMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static UserDto toModel(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    public static UserWithOrdersDto toUserWithOrdersDto(User user) {
        UserWithOrdersDto userWithOrders = modelMapper.map(user, UserWithOrdersDto.class);

        userWithOrders.setOrders(user.getOrders().stream()
                .map(order -> {
                    OrderWithItemsDto orderWithItemsDto = modelMapper
                            .map(order, OrderWithItemsDto.class);

                    orderWithItemsDto.setItems(order.getItems().stream()
                            .map(OrderItem::getItem)
                            .map(item -> modelMapper.map(item, EbayItemDto.class))
                            .toList());
                    return orderWithItemsDto;
                })
                .toList());

        return userWithOrders;
    }
}
