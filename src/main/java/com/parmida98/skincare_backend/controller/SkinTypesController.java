package com.parmida98.skincare_backend.controller;

import com.parmida98.skincare_backend.dto.SkinTypeDTO;
import com.parmida98.skincare_backend.service.SkinTypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/skin-types")
public class SkinTypesController {

    private final SkinTypeService skinTypeService;

    public SkinTypesController(SkinTypeService skinTypeService) {
        this.skinTypeService = skinTypeService;
    }

    @GetMapping
    public List<SkinTypeDTO> getAll() {
        return skinTypeService.getAllSkinTypes();
        /*
        Frontend anropar /api/v1/skin-types
        SkinTypeController tar emot requesten
        Controllern säger: “Service, ge mig alla hudtyper”
        Service skapar/hämtar listan
        Controllern returnerar svaret som JSON
         */
    }
}
