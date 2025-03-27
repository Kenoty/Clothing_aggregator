package com.project.clothingaggregator.mapper;

import com.project.clothingaggregator.dto.ProductDto;
import com.project.clothingaggregator.dto.ProductRequest;
import com.project.clothingaggregator.entity.Product;
import com.project.clothingaggregator.exception.NotFoundException;
import org.modelmapper.ModelMapper;

import java.util.Optional;

public class ProductMapper {

    public final static ModelMapper modelMapper = new ModelMapper();

    public static Product toEntity(ProductRequest request) {
        Product product = new Product();
        product.setExternalReferenceId(request.getExternalId());
        product.setSourceSystem(request.getSourceSystem());
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setImageUrl(request.getImageUrl());
        return product;
    }

    public static Product updateFromRequest(ProductRequest request, Optional<Product> optionalProduct) {
        Product existingProduct = optionalProduct.orElseThrow(() ->
                new NotFoundException("User not found"));
        existingProduct.setName(request.getName());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setImageUrl(request.getImageUrl());
        return existingProduct;
    }

    public static ProductDto toResponse(Product product) {
        return modelMapper.map(product, ProductDto.class);
    }
}
