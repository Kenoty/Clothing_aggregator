package com.project.clothingaggregator.mapper;

import com.project.clothingaggregator.dto.EbayItemDto;
import com.project.clothingaggregator.dto.ItemSummary;
import com.project.clothingaggregator.entity.EbayClothingItem;
import org.modelmapper.ModelMapper;

public class EbayItemMapper {
    public static final ModelMapper modelMapper = new ModelMapper();

    public static EbayClothingItem toEntity(ItemSummary item) {
        EbayClothingItem ebayItem = modelMapper.map(item, EbayClothingItem.class);
        ebayItem.setImageUrl(item.getImage().getImageUrl());
        return ebayItem;
    }

    public static EbayItemDto toDto(EbayClothingItem item) {
        EbayItemDto dto = modelMapper.map(item, EbayItemDto.class);
        dto.getImage().setImageUrl(item.getImageUrl());
        return dto;
    }
}
