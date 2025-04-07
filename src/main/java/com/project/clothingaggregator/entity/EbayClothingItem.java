package com.project.clothingaggregator.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ebay_items")
public class EbayClothingItem {
    @Id
    private String itemId;

    private String title;
    @Column(nullable = false)
    private String brand;
    @Column(nullable = false)
    private String category;

    @Column(length = 1000)
    private String imageUrl;

    private boolean isActive;
    private LocalDateTime lastUpdated;
}
