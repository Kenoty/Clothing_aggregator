package com.project.clothingaggregator.service;

import com.project.clothingaggregator.entity.EbayClothingItem;
import com.project.clothingaggregator.entity.User;
import com.project.clothingaggregator.entity.UserFavorite;
import com.project.clothingaggregator.entity.UserFavoriteId;
import com.project.clothingaggregator.exception.AlreadyExistsException;
import com.project.clothingaggregator.exception.NotFoundException;
import com.project.clothingaggregator.repository.ItemRepository;
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
    private final ItemRepository itemRepository;

    @Transactional
    public UserFavorite addToFavorites(Integer userId, String itemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        final EbayClothingItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        if (userFavoriteRepository.existsById(new UserFavoriteId(userId, itemId))) {
            throw new AlreadyExistsException("Product already in favorites");
        }

        UserFavorite favorite = new UserFavorite();
        favorite.setId(new UserFavoriteId(userId, itemId));
        favorite.setUser(user);
        favorite.setItem(item);

        return userFavoriteRepository.save(favorite);
    }

    @Transactional(readOnly = true)
    public List<UserFavorite> getUserFavorites(Integer userId) {
        return userFavoriteRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public boolean isFavorite(Integer userId, String itemId) {
        return userFavoriteRepository.existsById(new UserFavoriteId(userId, itemId));
    }

    @Transactional
    public void removeFromFavorites(Integer userId, String itemId) {
        userFavoriteRepository.deleteById(new UserFavoriteId(userId, itemId));
    }

    @Transactional(readOnly = true)
    public long countUserFavorites(Integer userId) {
        return userFavoriteRepository.countByUserId(userId);
    }
}
