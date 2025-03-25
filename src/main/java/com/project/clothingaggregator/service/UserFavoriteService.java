package com.project.clothingaggregator.service;

import com.project.clothingaggregator.dto.UserFavoriteRequest;
import com.project.clothingaggregator.entity.Product;
import com.project.clothingaggregator.entity.User;
import com.project.clothingaggregator.entity.UserFavorite;
import com.project.clothingaggregator.entity.UserFavoriteId;
import com.project.clothingaggregator.exception.NotFoundException;
import com.project.clothingaggregator.repository.ProductRepository;
import com.project.clothingaggregator.repository.UserFavoriteRepository;
import com.project.clothingaggregator.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserFavoriteService {

    private final UserFavoriteRepository userFavoriteRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public UserFavorite addToFavorites(UserFavoriteRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        UserFavoriteId id = new UserFavoriteId(request.getUserId(), request.getProductId());

        if (userFavoriteRepository.existsById(id)) {
            throw new IllegalStateException("Product already in favorites");
        }

        UserFavorite favorite = new UserFavorite();
        favorite.setId(id);
        favorite.setUser(user);
        favorite.setProduct(product);

        return userFavoriteRepository.save(favorite);
    }

    @Transactional
    public void removeFromFavorites(UserFavoriteRequest request) {
        if (!userFavoriteRepository.existsById_UserIdAndId_ProductId(request.getUserId(), request.getProductId())) {
            throw new NotFoundException("Favorite not found");
        }
        userFavoriteRepository.deleteById_UserIdAndId_ProductId(request.getUserId(), request.getProductId());
    }

    public List<UserFavorite> getUserFavorites(Integer userId) {
        return userFavoriteRepository.findAllByUserId(userId);
    }

    public boolean isFavorite(UserFavoriteRequest request) {
        return userFavoriteRepository.existsById_UserIdAndId_ProductId(request.getUserId(), request.getProductId());
    }
}
