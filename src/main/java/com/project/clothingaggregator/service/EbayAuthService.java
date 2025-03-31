package com.project.clothingaggregator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.project.clothingaggregator.config.EbayConfig;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class EbayAuthService {

    private final WebClient webClient;
    private final EbayConfig ebayConfig;

    public EbayAuthService(WebClient ebayWebClient, EbayConfig ebayConfig) {
        this.webClient = ebayWebClient;
        this.ebayConfig = ebayConfig;
    }

    public Mono<String> getAccessToken() {
        return webClient.post()
                .uri("/identity/v1/oauth2/token")
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
                .map(response -> {
                    if (!response.has("access_token")) {
                        throw new RuntimeException("No access_token in eBay response");
                    }
                    return response.get("access_token").asText();
                })
                .doOnNext(token -> System.out.println("Successfully obtained eBay token"))
                .doOnError(e -> System.err.println("Error getting eBay token: " + e.getMessage()));
    }
}
