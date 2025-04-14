package com.project.clothingaggregator.service;

import com.project.clothingaggregator.entity.Product;
import com.project.clothingaggregator.entity.User;
import com.project.clothingaggregator.entity.UserFavorite;
import com.project.clothingaggregator.entity.UserFavoriteId;
import com.project.clothingaggregator.exception.AlreadyExistsException;
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
    public UserFavorite addToFavorites(Integer userId, Integer productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

//        final Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new NotFoundException("Product not found"));

        if (userFavoriteRepository.existsById(new UserFavoriteId(userId, productId))) {
            throw new AlreadyExistsException("Product already in favorites");
        }

        UserFavorite favorite = new UserFavorite();
        favorite.setId(new UserFavoriteId(userId, productId));
        favorite.setUser(user);
        //favorite.setProduct(product);

        return userFavoriteRepository.save(favorite);
    }

    @Transactional(readOnly = true)
    public List<UserFavorite> getUserFavorites(Integer userId) {
        return userFavoriteRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public boolean isFavorite(Integer userId, Integer productId) {
        return userFavoriteRepository.existsById(new UserFavoriteId(userId, productId));
    }

    @Transactional
    public void removeFromFavorites(Integer userId, Integer productId) {
        userFavoriteRepository.deleteById(new UserFavoriteId(userId, productId));
    }

    @Transactional(readOnly = true)
    public long countUserFavorites(Integer userId) {
        return userFavoriteRepository.countByUserId(userId);
    }
}
