package com.project.clothingaggregator.dto;

import com.project.clothingaggregator.entity.Order;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderItemRequest {
    private Order order;
    private String itemId;
}