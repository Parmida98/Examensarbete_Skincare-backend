package com.parmida98.skincare_backend.service.mapper;

import com.parmida98.skincare_backend.dto.IngredientDTO;
import com.parmida98.skincare_backend.entities.IngredientEntity;
import org.springframework.stereotype.Component;

// konvertera JPA-entities till Api DTOs
// gör API:t stabilt även om db ändras

@Component
public class IngredientMapper {

    public IngredientDTO toDto(IngredientEntity entity) {
        return new IngredientDTO(
                entity.getInciName(),
                entity.getDescription()
        );
    }
}
