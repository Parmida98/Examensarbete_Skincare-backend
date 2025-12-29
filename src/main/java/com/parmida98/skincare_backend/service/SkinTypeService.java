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

    private final SkinTypeRepository skinTypeRepository;

    public SkinTypeService(SkinTypeRepository skinTypeRepository) {
        this.skinTypeRepository = skinTypeRepository;
    }

    public List<SkinTypeDTO> getAllSkinTypes() {
        return skinTypeRepository.findAll()     // Hämtar rader från tabellen skin_type. JPA gör varje rad till ett Entity-objekt:
                .stream                         // filtrera, mappa, sortera o samla resultat
                .map(ste -> new SkinTypeDTO(     // “Gör om varje element till något annat”, från SkinTypeEntity till dto
                        ste.getLabel(),
                        ste.getTypes(),
                        ste.getDescription()
                ))
                .sorted((a, b) -> a.code().compareToIgnoreCase(b.code())) // sorterar resultatet alfabetiskt
                .toList();
    }
}
