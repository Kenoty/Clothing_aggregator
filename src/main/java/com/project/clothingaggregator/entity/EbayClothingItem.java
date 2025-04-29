package com.project.clothingaggregator.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
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

    @Column(name = "category", nullable = false)
    private String categoryPath;

    @Column(name = "image_url", length = 1000)
    private String imageUrl;

    @Column(name = "item_web_url", length = 1000)
    private String itemWebUrl;

    private LocalDateTime lastUpdated;

    @OneToMany(mappedBy = "item")
    private List<OrderItem> orderItems;

    @OneToMany(mappedBy = "item")
    private List<UserFavorite> favorites;
}
