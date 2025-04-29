package com.project.clothingaggregator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.project.clothingaggregator.config.EbayConfig;
import com.project.clothingaggregator.dto.EbaySearchResponse;
import com.project.clothingaggregator.dto.ItemSummary;
import com.project.clothingaggregator.entity.EbayClothingItem;
import com.project.clothingaggregator.mapper.EbayItemMapper;
import com.project.clothingaggregator.repository.ItemRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class EbayService {

    private final WebClient webClient;
    private final EbayConfig ebayConfig;
    private final ItemRepository itemRepository;
    private static final String EBAY_API_URL = "https://api.sandbox.ebay.com/buy/browse/v1/item_summary/search";
    private static final String CLOTHING_CATEGORY_ID = "11450";

    public Mono<String> getAccessToken() {
        return webClient.post()
                .uri(ebayConfig.getAuthUrl())
                .header(HttpHeaders.AUTHORIZATION, ebayConfig.getAuthHeader())
                .bodyValue("grant_type=client_credentials&scope=https://api.ebay.com/oauth/api_scope")
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new RuntimeException(
                                        "Failed to get eBay token: " + response
                                                .statusCode() + " - " + errorBody
                                )))
                )
                .bodyToMono(JsonNode.class)
                .flatMap(response -> {
                    if (!response.has("access_token")) {
                        return Mono.error(new RuntimeException("No access_token in eBay response"));
                    }
                    return Mono.just(response.get("access_token").asText());
                })
                .doOnNext(token -> System.out.println("Successfully obtained eBay token" + token))
                .doOnError(e -> System.err.println("Error getting eBay token: " + e.getMessage()));
    }

    public Mono<ItemSummary> fetchProductDetails(String itemId, String token) {
        String url = ebayConfig.getBaseUrl() + "/buy/browse/v1/item/" + itemId;
        return webClient.get()
                .uri(url)
                .header("Authorization", "Bearer " + token)
                .header("X-EBAY-C-MARKETPLACE-ID", ebayConfig.getMarketplaceId())
                .retrieve()
                .bodyToMono(ItemSummary.class)
                .timeout(Duration.ofSeconds(5))
                .onErrorResume(e -> {
                    System.err.println("Ebay API error for item " + itemId + ": " + e.getMessage());
                    return Mono.empty();
                });
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void updateCatalog() {
        getAccessToken()
                .flatMap(token -> getItemSummaries(token)
                        .flatMap(summaries -> processItems(summaries, token)))
                .subscribe();
    }

    private Mono<ResponseEntity<EbaySearchResponse>> getItemSummaries(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("X-EBAY-C-MARKETPLACE-ID", ebayConfig.getMarketplaceId());

        String url = EBAY_API_URL + "?category_ids=" + CLOTHING_CATEGORY_ID + "&limit=200";

        return WebClient.create()
                .get()
                .uri(url)
                .headers(h -> h.addAll(headers))
                .retrieve()
                .toEntity(EbaySearchResponse.class);
    }

    private Mono<Void> processItems(ResponseEntity<EbaySearchResponse> response, String token) {
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            return Mono.error(new RuntimeException("API error: " + response.getStatusCode()));
        }

        List<String> itemIds = Arrays.stream(response.getBody().getItemSummaries())
                .map(ItemSummary::getItemId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return Flux.fromIterable(itemIds)
                .flatMap(id -> fetchProductDetails(id, token))
                .collectList()
                .flatMap(this::saveOrUpdateClothingItems);
    }

    private Mono<Void> saveOrUpdateClothingItems(List<ItemSummary> items) {
        if (items == null || items.isEmpty()) {
            return Mono.empty();
        }

        return Mono.fromRunnable(() -> {
            items.forEach(ebayItem -> {
                try {
                    EbayClothingItem ebayClothingItem = EbayItemMapper.toEntity(ebayItem);

                    Optional<EbayClothingItem> existingItem = itemRepository
                            .findById(ebayItem.getItemId());

                    if (existingItem.isPresent()) {
                        EbayClothingItem itemToUpdate = existingItem.get();
                        updateCatalogItemFields(itemToUpdate, ebayClothingItem);
                        itemRepository.save(itemToUpdate);
                    } else {
                        itemRepository.save(ebayClothingItem);
                    }
                } catch (Exception e) {
                    System.err.println("Error processing item "
                            + ebayItem.getItemId() + ":" + e.getMessage());
                }
            });
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }

    private void updateCatalogItemFields(EbayClothingItem existing, EbayClothingItem newData) {
        if (!Objects.equals(existing.getTitle(), newData.getTitle())) {
            existing.setTitle(newData.getTitle());
        }

        if (!Objects.equals(existing.getBrand(), newData.getBrand())) {
            existing.setBrand(newData.getBrand());
        }

        if (!Objects.equals(existing.getCategoryPath(), newData.getCategoryPath())) {
            existing.setCategoryPath(newData.getCategoryPath());
        }

        if (!Objects.equals(existing.getImageUrl(), newData.getImageUrl())) {
            existing.setImageUrl(newData.getImageUrl());
        }

        if (!Objects.equals(existing.getItemWebUrl(), newData.getItemWebUrl())) {
            existing.setItemWebUrl(newData.getItemWebUrl());
        }
        existing.setLastUpdated(LocalDateTime.now());
    }
}
