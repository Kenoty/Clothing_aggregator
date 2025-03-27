package com.project.clothingaggregator.mapper;

import com.project.clothingaggregator.dto.UserFavoriteDto;
import com.project.clothingaggregator.entity.UserFavorite;

public class UserFavoriteMapper {

    public final static UserFavoriteDto toResponse(UserFavorite favorite) {
        UserFavoriteDto dto = new UserFavoriteDto();
        dto.setUserId(favorite.getUser().getId());
        dto.setProductId(favorite.getProduct().getProductId());
        return dto;
    }
}
