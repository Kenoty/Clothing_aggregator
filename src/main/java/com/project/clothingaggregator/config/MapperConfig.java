package com.project.clothingaggregator.config;

import com.project.clothingaggregator.dto.ProductDto;
import com.project.clothingaggregator.entity.Product;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

//        modelMapper.typeMap(Product.class, ProductDto.class)
//                .addMappings(mapper -> {
//                    mapper.map(Product::getProductId, ProductDto::setProductId);
//                });

        return modelMapper;
    }
}
