package com.parmida98.skincare_backend;

import com.parmida98.skincare_backend.controller.IngredientController;
import com.parmida98.skincare_backend.error_handling.GlobalExceptionHandler;
import com.parmida98.skincare_backend.service.IngredientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = IngredientController.class)
@Import(GlobalExceptionHandler.class)
public class IngredientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IngredientService ingredientService;

    @Test
    void getIngredients_shouldReturn400_whenSkinTypeMissing() throws Exception {

        mockMvc.perform(get("/ingredients")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())

                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message", containsString("Missing required parameter: skinType")))
                .andExpect(jsonPath("$.path").value("/ingredients"))
                .andExpect(jsonPath("$.violations").isArray());

        verifyNoInteractions(ingredientService);
    }

    @Test
    void getIngredients_shouldReturn400_whenServiceThrowsIllegalArgument() throws Exception {

        when(ingredientService.getIngredientsBySkinType(eq("INVALID"), any(), any(Pageable.class)))
                .thenThrow(new IllegalArgumentException("Invalid skinType: INVALID"));

        mockMvc.perform(get("/ingredients")
                        .param("skinType", "INVALID")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())

                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Invalid skinType: INVALID"))
                .andExpect(jsonPath("$.path").value("/ingredients"));

        verify(ingredientService, times(1))
                .getIngredientsBySkinType(eq("INVALID"), isNull(), any(Pageable.class));
    }
}