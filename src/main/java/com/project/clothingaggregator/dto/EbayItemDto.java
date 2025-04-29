package com.project.clothingaggregator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EbayItemDto {
    private String itemId;
    private String title;
    private Price price;
    private Image image;
    private String brand;
    private String categoryPath;
    private String itemWebUrl;
}
