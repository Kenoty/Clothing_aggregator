package com.project.clothingaggregator.dto;

import java.util.List;
import lombok.Data;

@Data
public class OrderWithProductsDto {
    private Integer orderId;
    private String status;
    private List<ProductDto> products;
}
