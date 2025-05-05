package com.project.clothingaggregator.controller;

import com.project.clothingaggregator.dto.EbayItemDto;
import com.project.clothingaggregator.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    @Operation(
            summary = "Create a new eBay item",
            description = "Creates a new item listing on eBay")
    public ResponseEntity<EbayItemDto> createItem(@Valid @RequestBody EbayItemDto itemRequest) {
        return ResponseEntity.ok(itemService.createItem(itemRequest));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get eBay item by ID",
            description = "Retrieves item details by its eBay item ID")
    public ResponseEntity<EbayItemDto> getItem(@PathVariable String id) {
        return ResponseEntity.ok(itemService.getItem(id));
    }

    @GetMapping("/all")
    @Operation(
            summary = "Get all eBay items",
            description = "Retrieves a list of all available eBay items")
    public ResponseEntity<List<EbayItemDto>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update eBay item",
            description = "Updates an existing eBay item listing")
    public ResponseEntity<EbayItemDto> updateItem(
            @PathVariable String id,
            @RequestBody EbayItemDto productRequest) {

        return ResponseEntity.ok(itemService.updateItem(id, productRequest));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete eBay item",
            description = "Removes an item listing from database")
    public ResponseEntity<Void> deleteItem(@PathVariable String id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
