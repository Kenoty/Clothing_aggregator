package com.project.clothingaggregator.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MyCache<K, V> extends LinkedHashMap<K, V> {
    private final Integer capacity;

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }
}
