package com.project.clothingaggregator.dto;

import com.project.clothingaggregator.entity.Order;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderItemRequest {
    private Order order;

    @NotBlank(message = "id must be not blank")
    private String itemId;
}