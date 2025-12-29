package com.parmida98.skincare_backend.controller;

import com.parmida98.skincare_backend.dto.IngredientDTO;
import com.parmida98.skincare_backend.service.IngredientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public List<IngredientDTO> getBySkinType(@RequestParam String skinType) {
        return ingredientService.getIngredientsBySkinType(skinType);
    }
}
