package com.project.clothingaggregator.controller;

import com.project.clothingaggregator.service.VisitCounterService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {
    private final VisitCounterService visitCounterService;

    @Value("${api.users-all.url}")
    private String apiUrl;

    @GetMapping("/visits")
    @Operation(summary = "Get visit counter",
            description = "Returns the total number of visits to the API.")
    public ResponseEntity<Integer> getVisitCounter() {
        return ResponseEntity.ok(visitCounterService.getVisitCount(apiUrl));
    }
}
