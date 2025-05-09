package com.project.clothingaggregator.config;

import java.util.Base64;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Getter
@Configuration
public class EbayConfig {

    @Value("${ebay.app.id}")
    private String appId;

    @Value("${ebay.cert.id}")
    private String certId;

    @Value("${ebay.base.url}")
    private String baseUrl;

    @Value("${ebay.auth.url}")
    private String authUrl;

    @Value("${marketplace.id}")
    private String marketplaceId;

    @Bean
    public WebClient ebayWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public String getAuthHeader() {
        return "Basic " + Base64.getEncoder().encodeToString((appId + ":" + certId).getBytes());
    }
}
