package com.project.clothingaggregator.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Image {
    @NotBlank(message = "image URL must be not blank")
    private String imageUrl;
}
