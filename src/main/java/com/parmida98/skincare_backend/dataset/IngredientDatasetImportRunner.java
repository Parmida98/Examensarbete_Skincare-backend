package com.parmida98.skincare_backend.dataset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
/*
startar importen av json filen. körs automatiskt när appen startar
 */

@Component // Spring ska skapa och hantera
@ConditionalOnProperty(prefix = "app.dataset.import", name = "enabled", havingValue = "true") // Skapa och kör denna bean bara om "true". gör beteendet konfigurationsstyrt
public class IngredientDatasetImportRunner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(IngredientDatasetImportRunner.class);

    private final IngredientDatasetImportService importService; // delegerar jobbet till service

    public IngredientDatasetImportRunner(IngredientDatasetImportService importService) {
        this.importService = importService;
    }

    // Läser värde från application.properties. Mm property finns -> använd den. Annars -> använd default (json)
    @Value("${app.dataset.import.path:classpath:datasets/ingredients.json}")
    private String datasetPath;

    @Override
    public void run(ApplicationArguments args) {
        logger.info("Dataset import enabled. Importing ingredients from: {}", datasetPath);
        importService.importFrom(datasetPath); // JSON läses, data mappas, DB uppdateras
    }
}
