package com.project.clothingaggregator.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;

@Service
public class VisitCounterService {
    private final Map<String, AtomicInteger> urlCounters = new ConcurrentHashMap<>();

    public void incrementCounter(String url) {
        urlCounters.computeIfAbsent(url, k -> new AtomicInteger(0)).incrementAndGet();
    }

    public Integer getVisitCount(String url) {
        return urlCounters.getOrDefault(url, new AtomicInteger(0)).get();
    }

    public Integer resetCounter(String url) {
        urlCounters.put(url, new AtomicInteger(0));
        return 0;
    }
}
