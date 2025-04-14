package com.project.clothingaggregator.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class OrderWithProductsDto {
    private Integer orderId;
    private String status;
    private BigDecimal totalAmount;
    private String shippingAddress;
    private List<ProductDto> products;
}
