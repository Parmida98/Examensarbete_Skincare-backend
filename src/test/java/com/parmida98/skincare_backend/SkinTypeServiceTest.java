package com.parmida98.skincare_backend;

import com.parmida98.skincare_backend.dto.SkinTypeDTO;
import com.parmida98.skincare_backend.entities.SkinTypeEntity;
import com.parmida98.skincare_backend.repository.SkinTypeRepository;
import com.parmida98.skincare_backend.service.SkinTypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SkinTypeServiceTest {

    @Mock
    private SkinTypeRepository skinTypeRepository;

    @InjectMocks
    private SkinTypeService skinTypeService;

    @Test
    void getAllSkinTypes_shouldMapEntitiesToDtos() {
        SkinTypeEntity dry = new SkinTypeEntity();
        dry.setLabel("DRY");
        dry.setTypes("Dry skin");
        dry.setDescription("Dry skin description");

        SkinTypeEntity oily = new SkinTypeEntity();
        oily.setLabel("OILY");
        oily.setTypes("Oily skin");
        oily.setDescription("Oily skin description");

        // Mock: repo returnerar osorterat (som en mock alltid gör)
        when(skinTypeRepository.findAll(any(Sort.class)))
                .thenReturn(List.of(oily, dry));

        List<SkinTypeDTO> result = skinTypeService.getAllSkinTypes();

        assertEquals(2, result.size());

        // Hitta DRY DTO oberoende av ordning
        SkinTypeDTO dryDto = result.stream()
                .filter(dto -> "DRY".equals(dto.label()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected DTO with label DRY"));

        assertEquals("Dry skin", dryDto.types());
        assertEquals("Dry skin description", dryDto.description());

        // Hitta OILY DTO oberoende av ordning
        SkinTypeDTO oilyDto = result.stream()
                .filter(dto -> "OILY".equals(dto.label()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected DTO with label OILY"));

        assertEquals("Oily skin", oilyDto.types());
        assertEquals("Oily skin description", oilyDto.description());
    }
}

