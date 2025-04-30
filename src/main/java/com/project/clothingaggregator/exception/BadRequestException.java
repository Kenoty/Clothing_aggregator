package com.project.clothingaggregator.exception;

import java.util.List;
import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {
    private final List<String> errors;

    public BadRequestException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

}