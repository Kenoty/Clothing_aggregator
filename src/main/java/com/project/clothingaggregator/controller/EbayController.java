package com.project.clothingaggregator.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.project.clothingaggregator.service.EbayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/ebay")
@RequiredArgsConstructor
public class EbayController {

    private final EbayService authService;
    private final WebClient webClient;

    @GetMapping("/search")
    public Mono<ResponseEntity<JsonNode>> searchItems(@RequestParam String query) {
        return authService.getAccessToken()
                .flatMap(accessToken ->
                        webClient.get()
                                .uri(uriBuilder -> uriBuilder
                                        .path("/buy/browse/v1/item_summary/search")
                                        .queryParam("q", query)
                                        .build())
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                                .header("X-EBAY-C-MARKETPLACE-ID", "EBAY_US")
                                .retrieve()
                                .onStatus(HttpStatusCode::isError, response ->
                                        response.bodyToMono(String.class)
                                                .flatMap(error -> Mono.error(new RuntimeException(
                                                        "eBay API error: " + response
                                                                .statusCode() + " - " + error
                                                )))
                                )
                                .bodyToMono(JsonNode.class)
                                .map(ResponseEntity::ok)
                                .onErrorResume(e -> Mono.just(
                                        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                                .body(null)
                                ))
                );
    }
}
