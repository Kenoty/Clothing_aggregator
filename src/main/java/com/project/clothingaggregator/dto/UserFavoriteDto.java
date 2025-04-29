package com.project.clothingaggregator.dto;

import lombok.Data;

@Data
public class UserFavoriteDto {
    private Integer userId;
    private EbayItemDto itemDto;
}