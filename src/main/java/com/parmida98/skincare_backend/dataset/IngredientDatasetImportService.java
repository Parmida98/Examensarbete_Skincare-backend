package com.parmida98.skincare_backend.dataset;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parmida98.skincare_backend.entities.IngredientEntity;
import com.parmida98.skincare_backend.entities.SkinTypeEntity;
import com.parmida98.skincare_backend.repository.IngredientRepository;
import com.parmida98.skincare_backend.repository.SkinTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

/*
samma INCI-namn ska inte skapas flera gånger
endast ändrad data ska uppdateras
befintlig data ska respekteras
timestamps ska uppdateras korrekt
 */

@Service
public class IngredientDatasetImportService {

    private static final Logger logger = LoggerFactory.getLogger(IngredientDatasetImportService.class);

    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;
    private final SkinTypeRepository skinTypeRepository;
    private final IngredientRepository ingredientRepository;

    public IngredientDatasetImportService(ObjectMapper objectMapper, ResourceLoader resourceLoader, SkinTypeRepository skinTypeRepository, IngredientRepository ingredientRepository) {
        this.objectMapper = objectMapper;
        this.resourceLoader = resourceLoader;
        this.skinTypeRepository = skinTypeRepository;
        this.ingredientRepository = ingredientRepository;
    }


    @Transactional // Allt i metoden körs som EN databas-transaktion. Allt lyckas -> db uppdateras. Något går fel -> inget sparas. För att db inte ska ha halv data, utna vara konsekvent
    public ImportResult importFrom(String datasetPath){
        try{
            Resource resource = resourceLoader.getResource(datasetPath); // Gör så att: classpath:, file:, http:, kan användas utan att ändra kod

            if(!resource.exists()){
                throw new IllegalArgumentException("Dataset does not exist: " + datasetPath);
            }

            // mappar varje objekt till IngredientDatasetItem
            List<IngredientDatasetItem> items = objectMapper.readValue(
                    resource.getInputStream(),
                    new TypeReference<List<IngredientDatasetItem>>(){}
            );

            // Cache: hämta alla skin types en gång
            Map<String, SkinTypeEntity> skinTypeCache = skinTypeRepository.findAll()
                    .stream()
                    .collect(java.util.stream.Collectors.toMap(
                            st -> st.getLabel().trim().toLowerCase(),
                            st -> st
                    ));

            int inserted = 0;
            int updated = 0;
            int skipped = 0;

            for (IngredientDatasetItem item : items){
                if (item == null || isBlank(item.inciName())){
                    skipped++;
                    continue;
                }

                // Tar bort: onödiga mellanslag
                String inciName = item.inciName().trim();
                String description = item.description() == null ? null : item.description().trim();

                // finns ingrediensen -> använd den. Annars -> skapa ny
                IngredientEntity entity = ingredientRepository.findByInciNameIgnoreCase(inciName)
                        .orElseGet(() -> {
                            IngredientEntity created = new IngredientEntity();
                            created.setInciName(inciName);
                            created.setCreatedAt(OffsetDateTime.now());
                            created.setUpdatedAt(OffsetDateTime.now());
                            return created;
                        });

                boolean isNew = (entity.getId() == null);

                boolean changed = false;

                // Uppdatera description bara om den faktiskt ändrats
                if (!isBlank(description) && !equalsIgnoreCaseSafe(description,entity.getDescription())){
                    entity.setDescription(description);
                    changed = true;
                }

                // Insert för nya ingredients
                if(isNew) {
                    ingredientRepository.save(entity);
                    inserted++;
                }

                boolean relationChanged = false;

                // skapar mapping mellan skin type och ingrediens
                if (item.skinTypes() != null) {
                    for (String stLabel : item.skinTypes()) {
                        if (isBlank(stLabel)) continue;

                        String key = stLabel.trim().toLowerCase();
                        SkinTypeEntity skinType = skinTypeCache.get(key);

                        if (skinType == null) {
                            throw new IllegalArgumentException("Unknown skin type: " + stLabel);
                        }

                        // synkar båda sidor i minnet
                        boolean added = skinType.addIngredient(entity);
                        if (added) {
                            relationChanged = true;
                        }
                    }
                }

                // Om relationer ändrats räknas det som ändring
                if (relationChanged) {
                    changed = true;
                }

                // Update ska ske max en gång per ingredient (och bara om den inte är ny)
                if (!isNew && changed) {
                    entity.setUpdatedAt(OffsetDateTime.now());
                    ingredientRepository.save(entity);
                    updated++;
                }

                // Skipped: bara om den inte är ny och inget ändrades
                if (!isNew && !changed) {
                    skipped++;
                }
            }
             logger.info("Ingredient dataset imported successfully. Total={}, Inserted={}, Updated={}, Skipped={}",
                     items.size(), inserted, updated, skipped);

            return new ImportResult(items.size(), inserted, updated, skipped);

        } catch (Exception e) {
            logger.error("Ingredient dataset import failed. (path={})", datasetPath, e);
            throw new IllegalStateException("Ingredient dataset import failed: " + datasetPath, e);
        }
    }

    private boolean isBlank(String str){
        return str == null || str.trim().isEmpty();
    }

    private boolean equalsIgnoreCaseSafe(String str1, String str2){
        if(str1 == null && str2 == null) return true;
        if(str1 == null || str2 == null) return false;
        return str1.trim().equalsIgnoreCase(str2.trim());
    }

    public record ImportResult(int total, int inserted, int updated, int skipped) {}

}
