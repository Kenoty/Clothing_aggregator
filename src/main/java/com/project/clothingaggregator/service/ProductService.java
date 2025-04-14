package com.project.clothingaggregator.service;

import com.project.clothingaggregator.dto.*;
import com.project.clothingaggregator.entity.EbayClothingItem;
import com.project.clothingaggregator.exception.NotFoundException;
import com.project.clothingaggregator.mapper.EbayItemMapper;
import com.project.clothingaggregator.repository.ProductRepository;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final EbayService ebayApi;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String EBAY_API_URL = "https://api.sandbox.ebay.com/buy/browse/v1/item_summary/search";
    private static final String CLOTHING_CATEGORY_ID = "11450";

//    public ProductDto createProduct(ProductRequest productRequest) {
//        return ProductMapper.toResponse(productRepository
//                .save(ProductMapper.toEntity(productRequest)));
//    }
//
//    public ProductDto getProduct(Integer id) {
//        return ProductMapper.toResponse(productRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException("User not found")));
//    }
//
//    public Page<ProductDto> getAllProducts(Pageable pageable) {
//        return productRepository.findAll(pageable).map(ProductMapper::toResponse);
//    }
//
//    public ProductDto updateProduct(Integer id, ProductRequest request) {
//        Product product = productRepository.findById(id).orElseThrow(NotFoundException::new);
//
//        return ProductMapper.toResponse(productRepository
//                .save(ProductMapper.updateFromRequest(request, product)));
//    }

//    public void deleteProduct(Integer id) {
//        if (!productRepository.existsById(id)) {
//            throw new NotFoundException("Product not found with id: " + id);
//        }
//        productRepository.deleteById(id);
//    }

    @Cacheable(value = "products", key = "#itemId")
    public ProductDto getProductDetails(String itemId) {
        EbayClothingItem item = productRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        EbayItemDto apiData = ebayApi.fetchProductDetails(itemId)
                .blockOptional()
                .orElse(null);

        return buildProductDto(item, apiData);
    }

    public List<ProductDto> searchProducts(String query, String brand, String category) {
        List<EbayClothingItem> items = productRepository.searchItems(query, brand, category);

        return items.parallelStream()
                .map(item -> {
                    EbayItemDto apiData = ebayApi.fetchProductDetails(item.getItemId())
                            .blockOptional()
                            .orElse(null);
                    return buildProductDto(item, apiData);
                })
                .collect(Collectors.toList());
    }

    private ProductDto buildProductDto(EbayClothingItem item, EbayItemDto apiData) {
        ProductDto.ProductDtoBuilder builder = ProductDto.builder()
                .itemId(item.getItemId())
                .title(item.getTitle())
                .brand(item.getBrand())
                .category(item.getCategory())
                .imageUrl(item.getImageUrl());

        if (apiData != null) {
            builder.currentPrice(apiData.getPrice().getValue())
                    .inStock(apiData.getInStock());
        }

        return builder.build();
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void updateCatalog() {
        ebayApi.getAccessToken()
                .flatMap(token -> {

                    HttpHeaders headers = new HttpHeaders();
                    headers.set("Authorization", "Bearer " + token);
                    headers.set("X-EBAY-C-MARKETPLACE-ID", "EBAY_US");

                    String url = EBAY_API_URL + "?category_ids=" + CLOTHING_CATEGORY_ID + "&limit=200";

                    return WebClient.create()
                            .get()
                            .uri(url)
                            .headers(h -> h.addAll(headers))
                            .retrieve()
                            .toEntity(EbaySearchResponse.class);
                })
                .doOnSuccess(response -> {
                    if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                        List<ItemSummary> items = Arrays.asList(response.getBody().getItemSummaries());
                        saveOrUpdateClothingItems(items);
                        System.out.println("Обновлено товаров: " + items.size());
                    } else {
                        System.err.println("Ошибка eBay API: " + response.getStatusCode());
                    }
                })
                .doOnError(error -> {
                    System.err.println("Ошибка при обновлении каталога: " + error.getMessage());
                    if (error instanceof WebClientResponseException.Unauthorized) {
                        System.err.println("Токен недействителен, требуется обновление!");
                    }
                })
                .subscribe();
    }

    private void saveOrUpdateClothingItems(List<ItemSummary> items) {
        if (items == null || items.isEmpty()) {
            return;
        }

        items.forEach(ebayItem -> {
            try {
                EbayClothingItem ebayClothingItem = EbayItemMapper.toEntity(ebayItem);

                Optional<EbayClothingItem> existingItem = productRepository
                        .findById(ebayItem.getItemId());

                if (existingItem.isPresent()) {
                    EbayClothingItem itemToUpdate = existingItem.get();
                    updateCatalogItemFields(itemToUpdate, ebayClothingItem);
                    productRepository.save(itemToUpdate);
//                    log.debug("Товар обновлен: {}", ebayItem.getItemId());
                } else {
                    productRepository.save(ebayClothingItem);
//                    log.debug("Товар добавлен: {}", ebayItem.getItemId());
                }
            } catch (Exception e) {
                System.err.println("Ошибка при обработке товара "
                        + ebayItem.getItemId() + ":" + e.getMessage());
            }
        });
    }

    private void updateCatalogItemFields(EbayClothingItem existing, EbayClothingItem newData) {
        if (!Objects.equals(existing.getTitle(), newData.getTitle())) {
            existing.setTitle(newData.getTitle());
        }

        if (!Objects.equals(existing.getBrand(), newData.getBrand())) {
            existing.setBrand(newData.getBrand());
        }

        if (!Objects.equals(existing.getCategory(), newData.getCategory())) {
            existing.setCategory(newData.getCategory());
        }

        if (!Objects.equals(existing.getImageUrl(), newData.getImageUrl())) {
            existing.setImageUrl(newData.getImageUrl());
        }
        existing.setLastUpdated(LocalDateTime.now());
    }
}
