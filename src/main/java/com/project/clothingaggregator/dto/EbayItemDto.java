package com.project.clothingaggregator.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EbayItemDto {
    @NotBlank(message = "id must be not blank")
    private String itemId;

    @NotBlank(message = "title must be not blank")
    private String title;
    private Price price;
    @Valid
    private Image image;

    @NotBlank(message = "brand must be not blank")
    private String brand;

    @NotBlank(message = "category path must be not blank")
    private String categoryPath;

    @NotBlank(message = "item wev URL must be not blank")
    private String itemWebUrl;
}
