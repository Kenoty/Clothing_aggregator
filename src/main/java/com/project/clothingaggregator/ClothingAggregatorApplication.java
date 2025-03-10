package com.project.clothingaggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ClothingAggregatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClothingAggregatorApplication.class, args);
    }
}
