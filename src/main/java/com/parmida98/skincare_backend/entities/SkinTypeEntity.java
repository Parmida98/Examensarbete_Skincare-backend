package com.parmida98.skincare_backend.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "skin_type")
public class SkinTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String label;

    @Column(nullable = false, length = 100)
    private String types;

    @Column(columnDefinition = "text")
    private String description;

    @ManyToMany
    @JoinTable(
            name = "skin_type_ingredient",
            joinColumns = @JoinColumn(name = "skin_type_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )

    private Set<IngredientEntity> ingredients = new HashSet<>();

    public SkinTypeEntity() {}

    public Long getId() {
        return id;
    }
    public String getLabel() {
        return label;
    }
    public String getTypes() {
        return types;
    }
    public String getDescription() {
        return description;
    }
    public Set<IngredientEntity> getIngredients() {
        return ingredients;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    public void setTypes(String types) {
        this.types = types;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public boolean addIngredient(IngredientEntity ingredient) {
        if (ingredient == null)
            return false;

        boolean added = ingredients.add(ingredient);
        if (added) {
            ingredient.getSkinTypes().add(this); // håller synkad i minnet
        }
        return  added;
    }
}
