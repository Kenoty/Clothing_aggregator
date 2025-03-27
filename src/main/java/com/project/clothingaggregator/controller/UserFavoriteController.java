package com.project.clothingaggregator.controller;

import com.project.clothingaggregator.dto.UserFavoriteDto;
import com.project.clothingaggregator.entity.UserFavorite;
import com.project.clothingaggregator.service.UserFavoriteService;
import com.project.clothingaggregator.mapper.UserFavoriteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class UserFavoriteController {

    private final UserFavoriteService userFavoriteService;

    @PostMapping
    public ResponseEntity<UserFavoriteDto> addToFavorites(
            @RequestParam Integer userId,
            @RequestParam Integer productId) {
        return ResponseEntity.ok(UserFavoriteMapper.toResponse(userFavoriteService
                .addToFavorites(userId, productId)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserFavoriteDto>> getUserFavorites(
            @PathVariable Integer userId) {
        return ResponseEntity.ok(userFavoriteService.getUserFavorites(userId)
                .stream().map(UserFavoriteMapper::toResponse).toList());
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkIfFavorite(
            @RequestParam Integer userId,
            @RequestParam Integer productId) {
        boolean isFavorite = userFavoriteService.isFavorite(userId, productId);
        return ResponseEntity.ok(isFavorite);
    }

    @DeleteMapping
    public ResponseEntity<Void> removeFromFavorites(
            @RequestParam Integer userId,
            @RequestParam Integer productId) {
        userFavoriteService.removeFromFavorites(userId, productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Long> countUserFavorites(
            @PathVariable Integer userId) {
        long count = userFavoriteService.countUserFavorites(userId);
        return ResponseEntity.ok(count);
    }
}