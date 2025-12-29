package com.parmida98.skincare_backend.error_handling;

import java.time.OffsetDateTime;
import java.util.List;

public record ApiErrorDTO (
        OffsetDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        List<FieldViolationDTO> violations
) {
    // ett fel på ett specifikt fält
    public record FieldViolationDTO(String field, String message) {}
}

