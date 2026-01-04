package com.parmida98.skincare_backend.controller;

import com.parmida98.skincare_backend.dto.IngredientDTO;
import com.parmida98.skincare_backend.dto.PageResponseDTO;
import com.parmida98.skincare_backend.service.IngredientService;
import com.parmida98.skincare_backend.service.mapper.PageResponseMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }


    @GetMapping
    public PageResponseDTO<IngredientDTO> getIngredients(                     // metadata: total pages, total elements, current page
            @RequestParam String skinType,                                    // för att filtrera ingredienser per hudtyp
            @RequestParam(required = false) String search,                   // för text-sökning i ingredienser
            @PageableDefault(size = 15, sort = "inciName") Pageable pageable
    ){
        // Skydd mot missbruk
        int safeSize =Math.min(pageable.getPageSize(), 50);
        Pageable safePageable = PageRequest.of(pageable.getPageNumber(), safeSize, pageable.getSort());

        // Hämtar Page från service
        Page<IngredientDTO> page =
                ingredientService.getIngredientsBySkinType(skinType, search, safePageable);

        // Konverterar till frontend-vänligt DTO
        return PageResponseMapper.fromPage(page);
    }
}
