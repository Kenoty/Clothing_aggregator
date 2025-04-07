package com.project.clothingaggregator.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EbayItemDto {
    private String itemId;
    private Price price;
    private Boolean inStock;
    private String imageUrl;
}
