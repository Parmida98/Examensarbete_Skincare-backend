package com.parmida98.skincare_backend.repository;

import com.parmida98.skincare_backend.entities.IngredientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IngredientRepository extends JpaRepository<IngredientEntity, Long> {
    Optional<IngredientEntity> findByInciNameIgnoreCase(String inciName);

    @Query("""
    SELECT DISTINCT i
    FROM IngredientEntity i
    JOIN i.skinTypes st
    WHERE st.label = :skinType
    AND ( :search IS NULL OR :search = '' OR
    LOWER(i.inciName) LIKE LOWER(CONCAT('%', :search, '%')) OR
    LOWER(i.description) LIKE LOWER(CONCAT('%', :search, '%')) 
        )  
    """)

    Page<IngredientEntity>findBySkinTypeWithSearch(
            @Param("skinType") String skinType,
            @Param("search") String search,
            Pageable pageable
    );
}