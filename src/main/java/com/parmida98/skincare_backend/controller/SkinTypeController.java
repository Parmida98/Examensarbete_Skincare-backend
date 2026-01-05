package com.parmida98.skincare_backend.controller;

import com.parmida98.skincare_backend.dto.SkinTypeDTO;
import com.parmida98.skincare_backend.service.SkinTypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/skin-types")
public class SkinTypeController {

    private final SkinTypeService skinTypeService;

    public SkinTypeController(SkinTypeService skinTypeService) {
        this.skinTypeService = skinTypeService;
    }

    @GetMapping
    public List<SkinTypeDTO> getAll() {
        return skinTypeService.getAllSkinTypes();
        /*
        Frontend anropar /skin-types
        SkinTypeController tar emot requesten
        Controllern säger: “Service, ge mig alla hudtyper”
        Service skapar/hämtar listan
        Controllern returnerar svaret som JSON
         */
    }
}
