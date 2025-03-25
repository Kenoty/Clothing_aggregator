package com.project.clothingaggregator.repository;

import com.project.clothingaggregator.entity.UserFavorite;
import com.project.clothingaggregator.entity.UserFavoriteId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFavoriteRepository extends JpaRepository<UserFavorite, UserFavoriteId> {

    @Query("SELECT uf FROM UserFavorite uf WHERE uf.user.id = :userId")
    List<UserFavorite> findAllByUserId(@Param("userId") Integer userId);

    boolean existsById_UserIdAndId_ProductId(Integer userId, Integer productId);

    void deleteById_UserIdAndId_ProductId(Integer userId, Integer productId);
}
