package com.project.clothingaggregator.service;

import com.project.clothingaggregator.dto.ProductDto;
import com.project.clothingaggregator.dto.ProductRequest;
import com.project.clothingaggregator.entity.Product;
import com.project.clothingaggregator.exception.NotFoundException;
import com.project.clothingaggregator.mapper.ProductMapper;
import com.project.clothingaggregator.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductDto createProduct(ProductRequest productRequest) {
        return ProductMapper.toResponse(productRepository
                .save(ProductMapper.toEntity(productRequest)));
    }

    public ProductDto getProduct(Integer id) {
        return ProductMapper.toResponse(productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found")));
    }

    public Page<ProductDto> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(ProductMapper::toResponse);
    }

    public ProductDto updateProduct(Integer id, ProductRequest request) {
        Product product = productRepository.findById(id).orElseThrow(NotFoundException::new);

        return ProductMapper.toResponse(productRepository
                .save(ProductMapper.updateFromRequest(request, product)));
    }

    public void deleteProduct(Integer id) {
        if (!productRepository.existsById(id)) {
            throw new NotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
}
