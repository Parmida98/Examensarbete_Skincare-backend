package com.parmida98.skincare_backend.dto;

import java.util.List;

public record PageResponseDTO<T>(
        List<T> items,
        int page,
        int size,
        long totalItems,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
) {
}
