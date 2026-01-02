package com.parmida98.skincare_backend.service;

import com.parmida98.skincare_backend.dto.IngredientDTO;
import com.parmida98.skincare_backend.entities.SkinTypeEntity;
import com.parmida98.skincare_backend.repository.SkinTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {

    private final SkinTypeRepository skinTypeRepository;
    private final SkinTypeService skinTypeService;

    @Autowired
    public IngredientService(SkinTypeRepository skinTypeRepository, SkinTypeService skinTypeService) {
        this.skinTypeRepository = skinTypeRepository;
        this.skinTypeService = skinTypeService;
    }

    public List<IngredientDTO> getIngredientsBySkinType(String skinTypeLabel){
        if (skinTypeLabel == null || skinTypeLabel.isEmpty()){
            throw new IllegalArgumentException("skinTypeLabel cannot be null or empty");
        }

        SkinTypeEntity skinType = skinTypeRepository.findByLabel(skinTypeLabel.trim())
                .orElseThrow(() -> new IllegalArgumentException("Unknown skinType: " + skinTypeLabel));


        return skinType.getIngredients()
                .stream()                                                                                        // bearbeta listan steg för steg. Stream låter: filtrera, mappa, sortera o samla resultat
                .map(i -> new IngredientDTO(i.getInciName(), i.getDescription()))                 // map betyder:“Gör om varje element till något annat”, alltså från entity till dto
                .sorted((a,b) -> a.inciName().compareToIgnoreCase(b.inciName()))    // sorterar resultatet alfabetiskt. a = första objektet, b = andra objektet
                .toList();
    }
}
