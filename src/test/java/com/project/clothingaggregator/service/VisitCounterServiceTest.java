package com.project.clothingaggregator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VisitCounterServiceTest {

    private VisitCounterService visitCounterService;

    @BeforeEach
    void setUp() {
        visitCounterService = new VisitCounterService();
    }

    @Test
    void incrementCounter_ShouldIncreaseCount() {
        String url = "http://test.api/users";

        assertEquals(0, visitCounterService.getVisitCount(url));

        visitCounterService.incrementCounter(url);
        assertEquals(1, visitCounterService.getVisitCount(url));

        visitCounterService.incrementCounter(url);
        assertEquals(2, visitCounterService.getVisitCount(url));
    }

    @Test
    void getVisitCount_ShouldReturnZeroForNewUrl() {
        String url = "http://unknown.api";

        assertEquals(0, visitCounterService.getVisitCount(url));
    }
}
