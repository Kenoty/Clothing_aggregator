package com.project.clothingaggregator.repository;

import com.project.clothingaggregator.entity.Order;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserId(Integer userId);

    @EntityGraph(attributePaths = {"items", "items.product"})
    @Query("SELECT o FROM Order o WHERE o.id IN :orderIds")
    List<Order> findOrdersWithItems(@Param("orderIds") List<Integer> orderIds);

    Order findOrderById(Integer orderId);
}
