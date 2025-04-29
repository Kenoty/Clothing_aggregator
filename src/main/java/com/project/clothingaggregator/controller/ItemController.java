package com.project.clothingaggregator.controller;

import com.project.clothingaggregator.dto.EbayItemDto;
import com.project.clothingaggregator.service.ItemService;
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
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<EbayItemDto> createItem(@RequestBody EbayItemDto itemRequest) {
        return ResponseEntity.ok(itemService.createItem(itemRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EbayItemDto> getItem(@PathVariable String id) {
        return ResponseEntity.ok(itemService.getItem(id));
    }

    @GetMapping
    public Page<EbayItemDto> getAllItems(Pageable pageable) {
        return itemService.getAllItems(pageable);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EbayItemDto> updateItem(
            @PathVariable String id,
            @RequestBody EbayItemDto productRequest) {

        return ResponseEntity.ok(itemService.updateItem(id, productRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable String id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
