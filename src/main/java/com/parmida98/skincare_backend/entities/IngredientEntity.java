package com.parmida98.skincare_backend.entities;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ingredient")
public class IngredientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "inci_name", nullable = false, unique = true, length = 200)
    private String inciName;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @ManyToMany(mappedBy = "ingredients")
    private Set<SkinTypeEntity> skinTypes = new HashSet<>();

    public Set<SkinTypeEntity> getSkinTypes() { return skinTypes; }

    public IngredientEntity() {}

    public Long getId() {
        return id;
    }
    public String getInciName() {
        return inciName;
    }
    public String getDescription() {
        return description;
    }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }

    public void setInciName(String inciName) {
        this.inciName = inciName;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
