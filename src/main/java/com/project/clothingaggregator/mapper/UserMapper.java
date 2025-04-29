package com.project.clothingaggregator.mapper;

import com.project.clothingaggregator.dto.*;
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

    public static UserWithFavoritesDto toUserWithFavorites(User user) {
        UserWithFavoritesDto userWithFavorites = new UserWithFavoritesDto();

        userWithFavorites.setUserId(user.getId());
        userWithFavorites.setBirthday(user.getBirthday());
        userWithFavorites.setEmail(user.getEmail());
        userWithFavorites.setUsername(user.getUsername());
        userWithFavorites.setFavorites(user.getFavorites().stream()
                .map(favorite -> EbayItemMapper.toDto(favorite.getItem())).toList());

        return userWithFavorites;
    }
}
