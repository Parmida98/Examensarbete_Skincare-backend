package com.parmida98.skincare_backend.dataset;

import java.util.List;

public record IngredientDatasetItem(
        String inciName,
        String description,
        List<String> skinTypes
) {
}
