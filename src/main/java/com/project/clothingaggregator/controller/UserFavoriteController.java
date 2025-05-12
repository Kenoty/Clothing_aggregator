package com.project.clothingaggregator.controller;

import com.project.clothingaggregator.dto.UserFavoriteDto;
import com.project.clothingaggregator.mapper.UserFavoriteMapper;
import com.project.clothingaggregator.service.UserFavoriteService;
import io.swagger.v3.oas.annotations.Operation;
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
    private final UserFavoriteMapper userFavoriteMapper;

    @PostMapping
    @Operation(
            summary = "Add item to favorites",
            description = "Adds a specific item to the user's favorites list")
    public ResponseEntity<UserFavoriteDto> addToFavorites(
            @RequestParam Integer userId,
            @RequestParam String itemId) {
        return ResponseEntity.ok(userFavoriteMapper.toResponse(userFavoriteService
                .addToFavorites(userId, itemId)));
    }

    @GetMapping("/user/{userId}")
    @Operation(
            summary = "Get user's favorites",
            description = "Retrieves all favorite items for a specific user")
    public ResponseEntity<List<UserFavoriteDto>> getUserFavorites(
            @PathVariable Integer userId) {
        return ResponseEntity.ok(userFavoriteService.getUserFavorites(userId)
                .stream().map(userFavoriteMapper::toResponse).toList());
    }

    @GetMapping("/check")
    @Operation(
            summary = "Check if item is favorite",
            description = "Checks if a specific item is in the user's favorites list")
    public ResponseEntity<Boolean> checkIfFavorite(
            @RequestParam Integer userId,
            @RequestParam String itemId) {
        boolean isFavorite = userFavoriteService.isFavorite(userId, itemId);
        return ResponseEntity.ok(isFavorite);
    }

    @DeleteMapping
    @Operation(
            summary = "Remove item from favorites",
            description = "Removes a specific item from the user's favorites list")
    public ResponseEntity<Void> removeFromFavorites(
            @RequestParam Integer userId,
            @RequestParam String itemId) {
        userFavoriteService.removeFromFavorites(userId, itemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}/count")
    @Operation(
            summary = "Count user's favorites",
            description = "Returns the total number of favorite items for a user")
    public ResponseEntity<Long> countUserFavorites(
            @PathVariable Integer userId) {
        long count = userFavoriteService.countUserFavorites(userId);
        return ResponseEntity.ok(count);
    }
}