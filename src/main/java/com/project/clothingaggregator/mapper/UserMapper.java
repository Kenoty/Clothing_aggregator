package com.project.clothingaggregator.mapper;

import com.project.clothingaggregator.dto.OrderWithProductsDto;
import com.project.clothingaggregator.dto.ProductDto;
import com.project.clothingaggregator.dto.UserWithOrdersDto;
import com.project.clothingaggregator.entity.OrderItem;
import com.project.clothingaggregator.entity.User;
import com.project.clothingaggregator.model.UserModel;
import org.modelmapper.ModelMapper;

public class UserMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static UserModel toModel(User user) {
        return modelMapper.map(user, UserModel.class);
    }

    public static UserWithOrdersDto toUserWithOrdersDto(User user) {
        UserWithOrdersDto userWithOrders = modelMapper.map(user, UserWithOrdersDto.class);

        userWithOrders.setOrders(user.getOrders().stream()
                .map(order -> {
                    OrderWithProductsDto orderWithProductsDto = modelMapper
                            .map(order, OrderWithProductsDto.class);

                    orderWithProductsDto.setProducts(order.getItems().stream()
                            .map(OrderItem::getProduct)
                            .map(product -> modelMapper.map(product, ProductDto.class))
                            .toList());
                    return orderWithProductsDto;
                })
                .toList());

        return userWithOrders;
    }
}
