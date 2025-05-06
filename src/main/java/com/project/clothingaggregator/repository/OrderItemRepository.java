package com.project.clothingaggregator.repository;

import com.project.clothingaggregator.entity.EbayClothingItem;
import com.project.clothingaggregator.entity.OrderItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findByOrderId(Integer orderId);

    @Query("""
        SELECT DISTINCT e FROM EbayClothingItem e
        JOIN e.orderItems oi
        JOIN oi.order o
        WHERE o.id = :orderId
        """)
    List<EbayClothingItem> findAllByOrderId(@Param("orderId")Integer orderId);
}
