package com.project.clothingaggregator.mapper;

import com.project.clothingaggregator.dto.EbayItemDto;
import com.project.clothingaggregator.entity.EbayClothingItem;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {

    public final ModelMapper modelMapper = new ModelMapper();

    public EbayClothingItem toEntity(EbayItemDto request) {
        EbayClothingItem item = modelMapper.map(request, EbayClothingItem.class);
        item.setImageUrl(request.getImage().getImageUrl());
        return item;
    }

    public EbayClothingItem updateFromRequest(EbayItemDto request, EbayClothingItem item) {
        item = modelMapper.map(request, EbayClothingItem.class);
        item.setImageUrl(request.getImage().getImageUrl());
        return item;
    }

    public EbayItemDto toResponse(EbayClothingItem item) {
        return modelMapper.map(item, EbayItemDto.class);
    }
}
