package com.project.clothingaggregator.mapper;

import com.project.clothingaggregator.entity.Order;
import com.project.clothingaggregator.entity.OrderItem;
import com.project.clothingaggregator.entity.Product;
import com.project.clothingaggregator.exception.NotFoundException;
import java.util.Optional;

public class OrderItemMapper {
    public static OrderItem toEntity(Product product, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setOrder(order);
        return orderItem;
    }
}
