package com.project.clothingaggregator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class OrderRequest {
    @Positive(message = "id must be positive")
    private Integer userId;

    @Pattern(regexp = "^\\p{L}{2,50}$", message = "incorrect name")
    private String status;

    @Positive(message = "total amount must be positive")
    private BigDecimal totalAmount;

    @NotBlank(message = "address must be not blank")
    private String shippingAddress;

}
