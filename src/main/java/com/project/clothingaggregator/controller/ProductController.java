package com.project.clothingaggregator.controller;

import com.project.clothingaggregator.dto.ProductDto;
import com.project.clothingaggregator.dto.ProductRequest;
import com.project.clothingaggregator.entity.Product;
import com.project.clothingaggregator.exception.NotFoundException;
import com.project.clothingaggregator.mapper.ProductMapper;
import com.project.clothingaggregator.repository.ProductRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductRequest productRequest) {
        return ResponseEntity.ok(ProductMapper.toResponse(productRepository
                .save(ProductMapper.toEntity(productRequest))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Integer id) {
        return ResponseEntity.ok(ProductMapper.toResponse(productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"))));
    }

    @GetMapping
    public Page<ProductDto> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(ProductMapper::toResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Integer id,
            @RequestBody ProductRequest productRequest) {

        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(ProductMapper.toResponse(productRepository
                .save(ProductMapper.updateFromRequest(productRequest, optionalProduct))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        if (!productRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
