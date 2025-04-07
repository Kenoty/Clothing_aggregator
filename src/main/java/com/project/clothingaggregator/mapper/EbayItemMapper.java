package com.project.clothingaggregator.mapper;

import com.project.clothingaggregator.dto.ItemSummary;
import com.project.clothingaggregator.dto.UserDto;
import com.project.clothingaggregator.entity.EbayClothingItem;
import com.project.clothingaggregator.entity.User;
import com.project.clothingaggregator.service.ProductService;
import org.modelmapper.ModelMapper;

public class EbayItemMapper {
    public static final ModelMapper modelMapper = new ModelMapper();

    public static EbayClothingItem toEntity(ItemSummary item) {
        return modelMapper.map(item, EbayClothingItem.class);
    }
}
