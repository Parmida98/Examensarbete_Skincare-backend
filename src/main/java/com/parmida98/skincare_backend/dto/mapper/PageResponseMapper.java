package com.parmida98.skincare_backend.dto.mapper;

import com.parmida98.skincare_backend.dto.PageResponseDTO;
import org.springframework.data.domain.Page;

public class PageResponseMapper {

    private PageResponseMapper() {}

    public static <T> PageResponseDTO<T> fromPage(Page<T> page) {
        return new PageResponseDTO<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.hasPrevious()
        );
    }
}
