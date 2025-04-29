package com.project.clothingaggregator.repository;

import com.project.clothingaggregator.entity.EbayClothingItem;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<EbayClothingItem, String> {

    @Query("SELECT c FROM EbayClothingItem c WHERE "
            + "(:brand IS NULL OR c.brand = :brand) AND "
            + "(:category IS NULL OR c.categoryPath = :category) AND "
            + "(:query IS NULL OR LOWER(c.title) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<EbayClothingItem> searchItems(
            @Param("query") String query,
            @Param("brand") String brand,
            @Param("category") String category
    );
}
