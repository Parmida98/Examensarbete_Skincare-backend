package com.parmida98.skincare_backend.service;

import com.parmida98.skincare_backend.dto.SkinTypeDTO;
import org.springframework.stereotype.Service;

import java.util.List;

/*
@service:
- Spring skapar en instans automatiskt
- Klassen kan injectas (användas) i andra klasser
- Slipper skapa objekt manuellt med new
 */
@Service
public class SkinTypeService {

    public List<SkinTypeDTO> getAllSkinTypes() {
        return List.of(
                new SkinTypeDTO("NORMAL", "Normal skin", "Neither particularly dry nor oily, usually balanced"),
                new SkinTypeDTO("DRY", "Dry skin", "May feel tight, dry and sometimes flaky"),
                new SkinTypeDTO("OILY", "Oily skin", "Produces more sebum and becomes slightly shiny, especially in the T-zone. May be more acne prone"),
                new SkinTypeDTO("COMBINATION", "Combination skin", "Mix of dry/normal and oily skin, often oily T-zone"),
                new SkinTypeDTO("SENSITIVE", "Sensitive skin", "Reacts easily with redness, burning or irritation")
        );
    }
}
