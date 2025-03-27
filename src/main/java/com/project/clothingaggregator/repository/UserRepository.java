package com.project.clothingaggregator.repository;

import com.project.clothingaggregator.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @EntityGraph(attributePaths = {"orders"})
    @Query("SELECT u FROM User u")
    Page<User> findAllWithOrders(Pageable pageable);
}
