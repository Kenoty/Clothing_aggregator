package com.project.clothingaggregator.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderRequest {
    private Integer userId;
    private String status;
    private BigDecimal totalAmount;
    private String shippingAddress;

}
