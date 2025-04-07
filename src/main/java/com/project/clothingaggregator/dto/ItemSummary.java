package com.project.clothingaggregator.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSummary {
    private String itemId;
    private String title;
    private Price price;
    private String imageUrl;
    private String brand;
    private String category;
}
