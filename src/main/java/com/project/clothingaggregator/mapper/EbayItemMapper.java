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
        EbayItemDto dto = new EbayItemDto();
        dto.setItemId(item.getItemId());
        dto.setTitle(item.getTitle());
        dto.getImage().setImageUrl(item.getImageUrl());
        dto.setBrand(item.getBrand());
        dto.setCategoryPath(item.getCategoryPath());
        dto.setItemWebUrl(item.getItemWebUrl());
        return dto;
    }
}
