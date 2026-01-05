package com.parmida98.skincare_backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Konfigurerar Jackson ObjectMapper
// Styr hur JSON serialiseras
// Används automatiskt av Spring när API svarar med JSON

@Configuration
public class MapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // findAndRegisterModules() räcker ofta för datumhantering.
                                                                        //→ Dubbelkolla om du verkligen behöver WRITE_DATES_AS_TIMESTAMPS-inställningen.
        return mapper;
    }
}

