package com.parmida98.skincare_backend.repository;

import com.parmida98.skincare_backend.entities.IngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IngredientRepository extends JpaRepository<IngredientEntity, Long> {
    Optional<IngredientEntity> findByInciName(String inciName);

    String inciName(String inciName);
}