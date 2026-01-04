package com.parmida98.skincare_backend.service;

import com.parmida98.skincare_backend.dto.IngredientDTO;
import com.parmida98.skincare_backend.entities.IngredientEntity;
import com.parmida98.skincare_backend.entities.SkinTypeEntity;
import com.parmida98.skincare_backend.repository.IngredientRepository;
import com.parmida98.skincare_backend.repository.SkinTypeRepository;
import com.parmida98.skincare_backend.service.mapper.IngredientMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class IngredientService {

    private final SkinTypeRepository skinTypeRepository;
    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;

    @Autowired
    public IngredientService(SkinTypeRepository skinTypeRepository, IngredientRepository ingredientRepository, IngredientMapper ingredientMapper) {
        this.skinTypeRepository = skinTypeRepository;
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
    }

    public Page<IngredientDTO> getIngredientsBySkinType(
            String skinTypeLabel,
            String search,
            Pageable pageable
    ) {
        // 1. grundvalidering
        if (skinTypeLabel == null || skinTypeLabel.isBlank()){
            throw new IllegalArgumentException("skinType is required");
        }

        // 2. validering mot db
        SkinTypeEntity skinType = skinTypeRepository
                .findByLabelIgnoreCase(skinTypeLabel.trim())
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid skinType: " + skinTypeLabel));

        // 3. Hämtar och filtrerar ingredienser från db och mappar till dto
        Page<IngredientEntity> page = ingredientRepository
                .findBySkinTypeWithSearch(skinType.getLabel(), search, pageable);

        return page.map(ingredientMapper::toDto); // Entity -> dto utan att tappa pagination-data
    }
}
