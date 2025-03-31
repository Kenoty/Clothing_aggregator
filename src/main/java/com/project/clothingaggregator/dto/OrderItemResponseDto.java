package com.project.clothingaggregator.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemResponseDto {
    private Integer orderId;
    private ProductDto productDto;
}
