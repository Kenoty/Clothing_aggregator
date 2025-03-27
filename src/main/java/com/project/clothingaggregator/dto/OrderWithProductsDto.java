package com.project.clothingaggregator.dto;

import java.util.List;
import lombok.Data;

@Data
public class OrderWithProductsDto {
    private Integer orderId;
    private String status;
    private String shippingAddress;
    private List<ProductDto> products;
}
