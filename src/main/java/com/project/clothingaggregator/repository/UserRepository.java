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

    @EntityGraph(attributePaths = {"favorites"})
    @Query("SELECT DISTINCT u FROM User u LEFT JOIN u.favorites")
    List<User> findAllWithFavorites();

    //    @Query(value = """
    //        SELECT DISTINCT * FROM users u
    //        JOIN user_favorites f on u.id = f.user_id
    //        JOIN ebay_items i on f.item_id = i.item_id
    //        WHERE brand = :brandName
    //        """, nativeQuery = true)

    @Query("""
        SELECT DISTINCT u FROM User u
        JOIN u.favorites f
        JOIN f.item i
        WHERE i.brand = :brandName
        """)
    List<User> getAllByFavoriteBrand(@Param("brandName") String brandName);
}
