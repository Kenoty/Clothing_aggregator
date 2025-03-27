package com.project.clothingaggregator.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponseDto {
    private Integer id;
    private String status;
    private BigDecimal totalAmount;
    private String shippingAddress;
}
