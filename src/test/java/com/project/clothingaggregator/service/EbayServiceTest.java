package com.project.clothingaggregator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.project.clothingaggregator.config.EbayConfig;
import com.project.clothingaggregator.dto.EbaySearchResponse;
import com.project.clothingaggregator.dto.ItemSummary;
import com.project.clothingaggregator.entity.EbayClothingItem;
import com.project.clothingaggregator.mapper.EbayItemMapper;
import com.project.clothingaggregator.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EbayServiceTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private EbayConfig ebayConfig;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private EbayItemMapper ebayItemMapper;

    @InjectMocks
    private EbayService ebayService;

    private final String testToken = "test-token";
    private final String testItemId = "12345";

    @Test
    void getAccessToken_Success() {
        JsonNode mockResponse = mock(JsonNode.class);
        when(mockResponse.has("access_token")).thenReturn(true);
        when(mockResponse.get("access_token")).thenReturn(mock(JsonNode.class));
        when(mockResponse.get("access_token").asText()).thenReturn(testToken);
        when(ebayConfig.getAuthUrl()).thenReturn("https://auth.url");
        when(ebayConfig.getAuthHeader()).thenReturn("Basic auth");

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(anyString())).thenAnswer(invocation -> requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(JsonNode.class)).thenReturn(Mono.just(mockResponse));

        StepVerifier.create(ebayService.getAccessToken())
                .expectNext(testToken)
                .verifyComplete();
    }

    @Test
    void fetchProductDetails_Success() {
        String testMarketplaceId = "EBAY_US";

        ItemSummary mockItem = new ItemSummary();
        mockItem.setItemId(testItemId);

        when(webClient.get()).thenAnswer(invocation -> requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(any(), any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ItemSummary.class)).thenReturn(Mono.just(mockItem));
        when(ebayConfig.getMarketplaceId()).thenReturn(testMarketplaceId);
        when(ebayConfig.getBaseUrl()).thenReturn("https://api.sandbox.ebay.com");

        StepVerifier.create(ebayService.fetchProductDetails(testItemId, testToken))
                .expectNextMatches(item -> testItemId.equals(item.getItemId()))
                .verifyComplete();
    }

    @Test
    void saveOrUpdateClothingItems_NewItem_SavesSuccessfully() {
        ItemSummary itemSummary = new ItemSummary();
        itemSummary.setItemId(testItemId);

        EbayClothingItem clothingItem = new EbayClothingItem();
        clothingItem.setItemId(testItemId);

        when(itemRepository.findById(testItemId)).thenReturn(Optional.empty());
        when(ebayItemMapper.toEntity(itemSummary)).thenReturn(clothingItem);
        when(itemRepository.save(clothingItem)).thenReturn(clothingItem);

        StepVerifier.create(ebayService.saveOrUpdateClothingItems(List.of(itemSummary)))
                .verifyComplete();

        verify(itemRepository).save(clothingItem);
    }

    @Test
    void saveOrUpdateClothingItems_ExistingItem_UpdatesFields() {
        ItemSummary itemSummary = new ItemSummary();
        itemSummary.setItemId(testItemId);
        itemSummary.setTitle("New Title");

        EbayClothingItem existingItem = new EbayClothingItem();
        existingItem.setItemId(testItemId);
        existingItem.setTitle("Old Title");

        EbayClothingItem updatedItem = new EbayClothingItem();
        updatedItem.setItemId(testItemId);
        updatedItem.setTitle("New Title");

        when(itemRepository.findById(testItemId)).thenReturn(Optional.of(existingItem));
        when(ebayItemMapper.toEntity(itemSummary)).thenReturn(updatedItem);
        when(itemRepository.save(existingItem)).thenReturn(existingItem);

        StepVerifier.create(ebayService.saveOrUpdateClothingItems(List.of(itemSummary)))
                .verifyComplete();

        verify(itemRepository).save(existingItem);
        assertEquals("New Title", existingItem.getTitle());
        assertNotNull(existingItem.getLastUpdated());
    }

    @Test
    void saveOrUpdateClothingItems_WhenItemsNull_ReturnsEmptyMono() {
        Mono<Void> result = ebayService.saveOrUpdateClothingItems(null);

        StepVerifier.create(result)
                .verifyComplete();

        verify(itemRepository, never()).findById(any());
        verify(itemRepository, never()).save(any());
    }

    @Test
    void saveOrUpdateClothingItems_WhenItemsEmpty_ReturnsEmptyMono() {
        Mono<Void> result = ebayService.saveOrUpdateClothingItems(List.of());

        StepVerifier.create(result)
                .verifyComplete();

        verify(itemRepository, never()).findById(any());
        verify(itemRepository, never()).save(any());
    }

    @Test
    void updateCatalogItemFields_OnlyUpdatesChangedFields() {
        EbayClothingItem existing = new EbayClothingItem();
        existing.setItemId(testItemId);
        existing.setTitle("Old Title");
        existing.setBrand("Old Brand");
        existing.setLastUpdated(LocalDateTime.now().minusDays(1));

        EbayClothingItem newData = new EbayClothingItem();
        newData.setItemId(testItemId);
        newData.setTitle("New Title");
        newData.setBrand("Old Brand");
        newData.setLastUpdated(LocalDateTime.now());

        ebayService.updateCatalogItemFields(existing, newData);

        assertEquals("New Title", existing.getTitle());
        assertEquals("Old Brand", existing.getBrand());
        assertTrue(existing.getLastUpdated().isAfter(LocalDateTime.now().minusMinutes(1)));
    }

    @Test
    void updateCatalogItemFields_UpdatesAllFields_WhenAllDifferent() {
        LocalDateTime initialTime = LocalDateTime.now().minusDays(1);
        EbayClothingItem existing = new EbayClothingItem();
        existing.setTitle("Old Title");
        existing.setBrand("Old Brand");
        existing.setCategoryPath("Old Path");
        existing.setImageUrl("Old Image");
        existing.setItemWebUrl("Old Web");
        existing.setLastUpdated(initialTime);

        EbayClothingItem newData = new EbayClothingItem();
        newData.setTitle("New Title");
        newData.setBrand("New Brand");
        newData.setCategoryPath("New Path");
        newData.setImageUrl("New Image");
        newData.setItemWebUrl("New Web");

        ebayService.updateCatalogItemFields(existing, newData);

        assertEquals("New Title", existing.getTitle());
        assertEquals("New Brand", existing.getBrand());
        assertEquals("New Path", existing.getCategoryPath());
        assertEquals("New Image", existing.getImageUrl());
        assertEquals("New Web", existing.getItemWebUrl());
        assertTrue(existing.getLastUpdated().isAfter(initialTime));
    }

    @Test
    void processItems_WithValidResponse_ProcessesAllItems() {
        EbaySearchResponse response = new EbaySearchResponse();
        ItemSummary[] summaries = {new ItemSummary(), new ItemSummary()};
        summaries[0].setItemId("id1");
        summaries[1].setItemId("id2");
        response.setItemSummaries(summaries);

        ResponseEntity<EbaySearchResponse> responseEntity =
                new ResponseEntity<>(response, HttpStatus.OK);

        when(webClient.get()).thenAnswer(invocation -> requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        ItemSummary mockItem1 = new ItemSummary();
        mockItem1.setItemId("id1");
        ItemSummary mockItem2 = new ItemSummary();
        mockItem2.setItemId("id2");

        when(webClient.get())
                .thenAnswer(invocation -> requestHeadersUriSpec)
                .thenAnswer(invocation -> requestHeadersUriSpec);

        when(requestHeadersUriSpec.uri(contains("/buy/browse/v1/item/")))
                .thenReturn(requestHeadersSpec);

        when(requestHeadersSpec.header("Authorization", "Bearer " + testToken))
                .thenReturn(requestHeadersSpec);

        when(requestHeadersSpec.header("X-EBAY-C-MARKETPLACE-ID", ebayConfig.getMarketplaceId()))
                .thenReturn(requestHeadersSpec);

        when(requestHeadersSpec.retrieve())
                .thenReturn(responseSpec);

        when(responseSpec.bodyToMono(ItemSummary.class))
                .thenReturn(Mono.just(mockItem1))
                .thenReturn(Mono.just(mockItem2));

        when(itemRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        lenient().when(itemRepository.save(any(EbayClothingItem.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        StepVerifier.create(ebayService.processItems(responseEntity, testToken))
                .verifyComplete();
    }

    @Test
    void getItemSummaries_Success() {
        EbaySearchResponse mockResponse = new EbaySearchResponse();
        ResponseEntity<EbaySearchResponse> responseEntity =
                new ResponseEntity<>(mockResponse, HttpStatus.OK);

        String expectedUrl = "https://api.sandbox.ebay.com/buy/browse/v1/item_summary/search?category_ids=11450&limit=200";

        when(ebayConfig.getMarketplaceId()).thenReturn("EBAY_US");

        WebClient mockWebClient = mock(WebClient.class);
        WebClient.RequestHeadersUriSpec<?> requestSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec<?> headersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        ReflectionTestUtils.setField(ebayService, "webClient", mockWebClient);

        when(mockWebClient.get()).thenAnswer(inv->requestSpec);
        when(requestSpec.uri(expectedUrl)).thenAnswer(inv -> headersSpec);
        when(headersSpec.headers(any(Consumer.class))).thenAnswer(invocation -> {
            Consumer<HttpHeaders> headersConsumer = invocation.getArgument(0);
            HttpHeaders headers = new HttpHeaders();
            headersConsumer.accept(headers);
            assertEquals("Bearer " + testToken, headers.getFirst("Authorization"));
            assertEquals("EBAY_US", headers.getFirst("X-EBAY-C-MARKETPLACE-ID"));
            return headersSpec;
        });
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(EbaySearchResponse.class)).thenReturn(Mono.just(responseEntity));

        StepVerifier.create(ebayService.getItemSummaries(testToken))
                .expectNext(responseEntity)
                .verifyComplete();
    }
}
