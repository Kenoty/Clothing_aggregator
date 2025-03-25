package com.project.clothingaggregator.mapper;

import com.project.clothingaggregator.dto.ProductRequest;
import com.project.clothingaggregator.entity.Product;

public class ProductMapper {
    public static Product toEntity(ProductRequest request) {
        Product product = new Product();
        product.setExternalId(request.getExternalId());
        product.setSourceSystem(request.getSourceSystem());
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setImageUrl(request.getImageUrl());
        return product;
    }
}
