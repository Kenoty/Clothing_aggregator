package com.project.clothingaggregator.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDto {
    private String itemId;
    private String title;
    private String brand;
    private String category;
    private String imageUrl;

    private String currentPrice;
    private Boolean inStock;
}
