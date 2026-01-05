package com.parmida98.skincare_backend;

import com.parmida98.skincare_backend.dto.IngredientDTO;
import com.parmida98.skincare_backend.entities.IngredientEntity;
import com.parmida98.skincare_backend.entities.SkinTypeEntity;
import com.parmida98.skincare_backend.repository.IngredientRepository;
import com.parmida98.skincare_backend.repository.SkinTypeRepository;
import com.parmida98.skincare_backend.service.IngredientService;
import com.parmida98.skincare_backend.service.SkinTypeService;
import com.parmida98.skincare_backend.service.mapper.IngredientMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IngredientServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;
    @Mock
    private SkinTypeRepository skinTypeRepository;
    @Mock
    private IngredientMapper ingredientMapper;
    @InjectMocks
    private IngredientService ingredientService;

    @Test
    void getIngredientBySkinType_shouldThrow_whenSkinTypeIsNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> ingredientService.getIngredientsBySkinType(null, null, PageRequest.of(0, 15))
        );

        assertEquals("skinType is required", ex.getMessage());
        verifyNoInteractions(skinTypeRepository, ingredientMapper, ingredientRepository);
    }

    @Test
    void getIngredientsBySkinType_shouldThrow_whenSkinTypeIsBlank() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> ingredientService.getIngredientsBySkinType("   ", "x", PageRequest.of(0, 15))
        );

        assertEquals("skinType is required", ex.getMessage());
        verifyNoInteractions(ingredientRepository, skinTypeRepository, ingredientMapper);
    }

    @Test
    void getIngredientsBySkinType_shouldThrow_whenSkinTypeNotFoundInDb() {
        PageRequest pageRequest = PageRequest.of(0, 15);

        when(skinTypeRepository.findByLabelIgnoreCase("dry"))
                .thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> ingredientService.getIngredientsBySkinType("dry", null, pageRequest)
        );

        assertTrue(ex.getMessage().contains("Invalid skinType"));
        verify(skinTypeRepository).findByLabelIgnoreCase("dry");
        verifyNoMoreInteractions(ingredientMapper, ingredientRepository);
    }

    @Test
    void getIngredientsBySkinType_shouldPassThroughSearch() {
        PageRequest  pageRequest = PageRequest.of(0, 10);

        SkinTypeEntity skinTypeEntity = new SkinTypeEntity();
        skinTypeEntity.setLabel("OILY");

        Page<IngredientEntity> entityPage = new PageImpl<>(List.of(), pageRequest, 0);

        when(skinTypeRepository.findByLabelIgnoreCase("OILY"))
                .thenReturn(Optional.of(skinTypeEntity));

        when(ingredientRepository.findBySkinTypeWithSearch(eq("OILY"), isNull(), eq(pageRequest)))
                .thenReturn(entityPage);

        Page<IngredientDTO> result = ingredientService.getIngredientsBySkinType("OILY", null, pageRequest);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        verify(ingredientRepository).findBySkinTypeWithSearch("OILY", null, pageRequest);
    }









}

