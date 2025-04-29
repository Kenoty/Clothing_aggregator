package com.project.clothingaggregator.mapper;

import com.project.clothingaggregator.dto.EbayItemDto;
import com.project.clothingaggregator.entity.EbayClothingItem;
import org.modelmapper.ModelMapper;

public class ItemMapper {

    public static final ModelMapper modelMapper = new ModelMapper();

    public static EbayClothingItem toEntity(EbayItemDto request) {
        EbayClothingItem item = modelMapper.map(request, EbayClothingItem.class);
        item.setImageUrl(request.getImage().getImageUrl());
        return item;
    }

    public static EbayClothingItem updateFromRequest(EbayItemDto request, EbayClothingItem item) {
        item = modelMapper.map(request, EbayClothingItem.class);
        item.setImageUrl(request.getImage().getImageUrl());
        return item;
    }

    public static EbayItemDto toResponse(EbayClothingItem item) {
        return modelMapper.map(item, EbayItemDto.class);
    }
}
