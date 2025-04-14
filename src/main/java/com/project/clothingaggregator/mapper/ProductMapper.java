package com.project.clothingaggregator.mapper;

import com.project.clothingaggregator.dto.ProductDto;
import com.project.clothingaggregator.dto.ProductRequest;
import com.project.clothingaggregator.entity.Product;
import com.project.clothingaggregator.exception.NotFoundException;
import java.util.Optional;
import org.modelmapper.ModelMapper;

public class ProductMapper {

    public static final ModelMapper modelMapper = new ModelMapper();

    public static Product toEntity(ProductRequest request) {
        Product product = new Product();
       // product.setExternalReferenceId(request.getExternalId());
        product.setSourceSystem(request.getSourceSystem());
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setImageUrl(request.getImageUrl());
        return product;
    }

    public static Product updateFromRequest(ProductRequest request, Product product) {
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setImageUrl(request.getImageUrl());
        return product;
    }

    public static ProductDto toResponse(Product product) {
        return modelMapper.map(product, ProductDto.class);
    }
}
