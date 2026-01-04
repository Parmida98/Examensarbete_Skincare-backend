package com.parmida98.skincare_backend.repository;

import com.parmida98.skincare_backend.entities.SkinTypeEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkinTypeRepository extends JpaRepository<SkinTypeEntity, Long> {

    @EntityGraph(attributePaths = "ingredients") // @EntityGraph gör att ingredients hämtas direkt
    Optional<SkinTypeEntity> findByLabelIgnoreCase(String label);
}
