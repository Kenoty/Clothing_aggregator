package com.project.clothingaggregator.config;

import com.project.clothingaggregator.dto.EbayItemDto;
import com.project.clothingaggregator.entity.UserFavorite;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addMappings(new PropertyMap<UserFavorite, EbayItemDto>() {
            @Override
            protected void configure() {
                map().setItemId(source.getItem().getItemId());
            }
        });

        return modelMapper;
    }
}
