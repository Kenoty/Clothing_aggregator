package com.project.clothingaggregator.mapper;

import com.project.clothingaggregator.dto.UserFavoriteDto;
import com.project.clothingaggregator.entity.UserFavorite;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFavoriteMapper {

    private final ItemMapper itemMapper;

    public UserFavoriteDto toResponse(UserFavorite favorite) {
        UserFavoriteDto dto = new UserFavoriteDto();
        dto.setUserId(favorite.getUser().getId());
        dto.setItemDto(itemMapper.toResponse(favorite.getItem()));
        return dto;
    }
}
