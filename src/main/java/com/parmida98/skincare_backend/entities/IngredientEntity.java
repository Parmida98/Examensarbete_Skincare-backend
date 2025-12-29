package com.parmida98.skincare_backend.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ingredient")
public class IngredientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "inci_name", nullable = false, unique = true, length = 200)
    private String inciName;

    @Column(columnDefinition = "text")
    private String description;

    @ManyToMany(mappedBy = "ingredients")
    private Set<SkinTypeEntity> skinTypes = new HashSet<>();

    public IngredientEntity() {}

    public long getId() {
        return id;
    }

    public String getInciName() {
        return inciName;
    }

    public void setInciName(String inciName) {
        this.inciName = inciName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
