package com.project.clothingaggregator.controller;

import com.project.clothingaggregator.dto.UserFavoriteDto;
import com.project.clothingaggregator.mapper.UserFavoriteMapper;
import com.project.clothingaggregator.service.UserFavoriteService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class UserFavoriteController {

    private final UserFavoriteService userFavoriteService;

    @PostMapping
    public ResponseEntity<UserFavoriteDto> addToFavorites(
            @RequestParam Integer userId,
            @RequestParam String itemId) {
        return ResponseEntity.ok(UserFavoriteMapper.toResponse(userFavoriteService
                .addToFavorites(userId, itemId)));
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
            @RequestParam String itemId) {
        boolean isFavorite = userFavoriteService.isFavorite(userId, itemId);
        return ResponseEntity.ok(isFavorite);
    }

    @DeleteMapping
    public ResponseEntity<Void> removeFromFavorites(
            @RequestParam Integer userId,
            @RequestParam String itemId) {
        userFavoriteService.removeFromFavorites(userId, itemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Long> countUserFavorites(
            @PathVariable Integer userId) {
        long count = userFavoriteService.countUserFavorites(userId);
        return ResponseEntity.ok(count);
    }
}