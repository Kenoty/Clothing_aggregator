package com.project.clothingaggregator.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductRequest {
    private String externalId;
    private String sourceSystem;
    private String name;
    private BigDecimal price;
    private String imageUrl;

}
