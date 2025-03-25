package com.project.clothingaggregator.mapper;

import com.project.clothingaggregator.entity.OrderItem;
import com.project.clothingaggregator.entity.Product;
import com.project.clothingaggregator.exception.NotFoundException;
import java.util.Optional;

public class OrderItemMapper {
    public static OrderItem toEntity(Optional<Product> product) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product.orElseThrow(() ->
                new NotFoundException("User not found")));
        return orderItem;
    }
}
