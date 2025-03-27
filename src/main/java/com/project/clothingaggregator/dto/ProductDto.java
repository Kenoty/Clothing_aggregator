package com.project.clothingaggregator.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ProductDto {
    private Integer productId;
    private String name;
    private BigDecimal price;
    private String imageUrl;
}
