package com.project.clothingaggregator.mapper;

import com.project.clothingaggregator.dto.*;
import com.project.clothingaggregator.entity.OrderItem;
import com.project.clothingaggregator.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final ModelMapper modelMapper = new ModelMapper();
    private final OrderMapper orderMapper = new OrderMapper();
    private final EbayItemMapper ebayItemMapper = new EbayItemMapper();

    public UserDto toModel(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    public UserWithOrdersDto toUserWithOrdersDto(User user) {
        UserWithOrdersDto userWithOrders = modelMapper.map(user, UserWithOrdersDto.class);

        userWithOrders.setOrders(user.getOrders().stream()
                .map(order ->
                        orderMapper.toOrderWithItems(order, order.getItems().stream()
                            .map(OrderItem::getItem)
                            .map(item -> modelMapper.map(item, EbayItemDto.class))
                            .toList())
                )
                .toList());

        return userWithOrders;
    }

    public UserWithFavoritesDto toUserWithFavorites(User user) {
        UserWithFavoritesDto userWithFavorites = new UserWithFavoritesDto();

        userWithFavorites.setUserId(user.getId());
        userWithFavorites.setBirthday(user.getBirthday());
        userWithFavorites.setEmail(user.getEmail());
        userWithFavorites.setUsername(user.getUsername());
        userWithFavorites.setFavorites(user.getFavorites().stream()
                .map(favorite -> ebayItemMapper.toDto(favorite.getItem())).toList());

        return userWithFavorites;
    }
}
