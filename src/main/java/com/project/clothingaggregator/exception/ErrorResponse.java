package com.project.clothingaggregator.exception;

import java.util.List;

public record ErrorResponse(String message, int status, List<String> errors) {}
