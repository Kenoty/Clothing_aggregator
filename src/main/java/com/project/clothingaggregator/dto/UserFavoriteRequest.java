package com.project.clothingaggregator.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserFavoriteRequest {
    private Integer userId;
    private Integer productId;

}
