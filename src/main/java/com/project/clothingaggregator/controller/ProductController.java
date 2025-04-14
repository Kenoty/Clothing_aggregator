package com.project.clothingaggregator.controller;

import com.project.clothingaggregator.dto.ProductDto;
import com.project.clothingaggregator.dto.ProductRequest;
import com.project.clothingaggregator.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

//    @PostMapping
//    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductRequest productRequest) {
//        return ResponseEntity.ok(productService.createProduct(productRequest));
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ProductDto> getProduct(@PathVariable Integer id) {
//        return ResponseEntity.ok(productService.getProduct(id));
//    }
//
//    @GetMapping
//    public Page<ProductDto> getAllProducts(Pageable pageable) {
//        return productService.getAllProducts(pageable);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<ProductDto> updateProduct(
//            @PathVariable Integer id,
//            @RequestBody ProductRequest productRequest) {
//
//        return ResponseEntity.ok(productService.updateProduct(id, productRequest));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
//        productService.deleteProduct(id);
//        return ResponseEntity.noContent().build();
//    }
}
