package com.project.clothingaggregator.repository;

import com.project.clothingaggregator.entity.UserFavorite;
import com.project.clothingaggregator.entity.UserFavoriteId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserFavoriteRepository extends JpaRepository<UserFavorite, UserFavoriteId> {

    @Query("SELECT uf FROM UserFavorite uf WHERE uf.id.userId = :userId")
    List<UserFavorite> findAllByUserId(@Param("userId") Integer userId);

    @Query("SELECT COUNT(uf) FROM UserFavorite uf WHERE uf.id.userId = :userId")
    long countByUserId(@Param("userId") Integer userId);
}