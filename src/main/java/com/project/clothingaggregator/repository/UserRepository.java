package com.project.clothingaggregator.repository;

import com.project.clothingaggregator.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @EntityGraph(attributePaths = {"orders"})
    @Query("SELECT DISTINCT u FROM User u LEFT JOIN u.orders")
    List<User> findAllWithOrders();

    @Query("""
        SELECT DISTINCT u FROM User u
        JOIN u.favorites f
        JOIN f.item i
        WHERE i.brand = :brandName
        """)
    List<User> getAllByFavoriteBrand(@Param("brandName") String brandName);
}